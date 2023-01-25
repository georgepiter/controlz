package br.com.controlz.controller;

import br.com.controlz.domain.dto.DebtValueDTO;
import br.com.controlz.domain.dto.RegisterDTO;
import br.com.controlz.domain.dto.ResponseEntityCustom;
import br.com.controlz.domain.exception.PhoneException;
import br.com.controlz.domain.exception.RegisterException;
import br.com.controlz.domain.exception.RegisterNotFoundException;
import br.com.controlz.domain.exception.ValueException;
import br.com.controlz.service.RegisterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@Api(value = "Registro", produces = MediaType.APPLICATION_JSON_VALUE, tags = {"Registro"})
@RequestMapping(value = "api/v1/register")
public class RegisterController {

	private final RegisterService registerService;

	public RegisterController(RegisterService registerService) {
		this.registerService = registerService;
	}

	@PostMapping(value = "/")
	@ApiOperation(value = "Método para salvar um novo registro")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
	public ResponseEntityCustom registerNewPerson(@NotNull @RequestBody RegisterDTO registerDTO) throws ValueException, RegisterException, PhoneException {
		return registerService.registerNewPerson(registerDTO);
	}

	@GetMapping(value = "/{userId}")
	@ApiOperation(value = "Obtém o registro pelo ID registro")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
	public RegisterDTO getRegisterById(@PathVariable Long userId) throws RegisterNotFoundException {
		return registerService.getRegisterById(userId);
	}

	@GetMapping(value = "/totalEntryValue/{userId}")
	@ApiOperation(value = "Obtém o valor de entrada + salário (Crédito)")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
	public DebtValueDTO getTotalEntryValue(@PathVariable Long userId) throws RegisterNotFoundException {
		return registerService.getTotalEntryValue(userId);
	}

	@GetMapping(value = "/currentEntryValue/{userId}")
	@ApiOperation(value = "Obtém o valor atualizado total de crédito - o valor dos débitos (despesas) do mês atual")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
	public DebtValueDTO getCurrentEntryValue(@PathVariable Long userId) throws RegisterNotFoundException {
		return registerService.getCurrentEntryValue(userId);
	}

	@PutMapping
	@ApiOperation(value = "Atualiza as informações do registro")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
	public ResponseEntity<HttpStatus> updateRegister(@NotNull @RequestBody RegisterDTO registerDTO) {
		return registerService.updateRegister(registerDTO);
	}

	@DeleteMapping(value = "/{userId}")
	@ApiOperation(value = "Deleta o registro e seus débitos pelo ID do Registro")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	public ResponseEntityCustom deleteRegister(@PathVariable Long userId) throws RegisterNotFoundException {
		return registerService.deleteRegister(userId);
	}

}
