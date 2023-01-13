package br.com.controlz.controller;

import br.com.controlz.domain.dto.DebtValueDTO;
import br.com.controlz.domain.dto.RegisterDTO;
import br.com.controlz.domain.dto.ResponseEntityCustom;
import br.com.controlz.domain.exception.*;
import br.com.controlz.service.RegisterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
	public ResponseEntityCustom registerNewPerson(@RequestBody RegisterDTO registerDTO) throws ValueException, RegisterException, PhoneException, EmailException, FieldException {
		return registerService.registerNewPerson(registerDTO);
	}

	@GetMapping(value = "/id/{id}")
	@ApiOperation(value = "Obtém o registro pelo ID registro")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
	public RegisterDTO getRegisterById(@PathVariable Long id) throws RegisterNotFoundException {
		return registerService.getRegisterById(id);
	}

	@GetMapping(value = "/all")
	@ApiOperation(value = "Obtém todos os registros")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
	public List<RegisterDTO> getAllRegisters() throws RegisterNotFoundException {
		return registerService.getAllRegisters();
	}

	@GetMapping(value = "/name/{name}")
	@ApiOperation(value = "Obtém o registro pelo nome cadastrado")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
	public RegisterDTO getRegister(@PathVariable String name) throws RegisterNotFoundException {
		return registerService.getRegister(name);
	}

	@GetMapping(value = "/totalEntryValue/{registerId}")
	@ApiOperation(value = "Obtém o valor de entrada + salário (Crédito)")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
	public DebtValueDTO getTotalEntryValue(@PathVariable Long registerId) throws RegisterNotFoundException {
		return registerService.getTotalEntryValue(registerId);
	}

	@GetMapping(value = "/currentEntryValue/{registerId}")
	@ApiOperation(value = "Obtém o valor atualizado total de crédito - o valor dos débitos (despesas)")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
	public DebtValueDTO getCurrentEntryValue(@PathVariable Long registerId) throws RegisterNotFoundException, DebtNotFoundException {
		return registerService.getCurrentEntryValue(registerId);
	}

	@PutMapping
	@ApiOperation(value = "Atualiza as informações do registro")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
	public ResponseEntity<HttpStatus> updateRegister(@RequestBody RegisterDTO registerDTO) {
		return registerService.updateRegister(registerDTO);
	}

	@DeleteMapping(value = "/id/{id}")
	@ApiOperation(value = "Deleta o registro e seus débitos pelo ID do Registro")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	public ResponseEntityCustom deleteRegister(@PathVariable Long id) throws RegisterNotFoundException {
		return registerService.deleteRegister(id);
	}

}
