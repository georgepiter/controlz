package br.com.controlz.controller;

import br.com.controlz.domain.dto.DebtDTO;
import br.com.controlz.domain.dto.DebtValueDTO;
import br.com.controlz.domain.exception.DebtNotFoundException;
import br.com.controlz.domain.exception.RegisterNotFoundException;
import br.com.controlz.service.DebtService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "Débito", produces = MediaType.APPLICATION_JSON_VALUE, tags = {"Débito"})
@RequestMapping(value = "api/v1/debt")
public class DebtController {

	private final DebtService debtService;

	public DebtController(DebtService debtService) {
		this.debtService = debtService;
	}

	@PostMapping(value = "/")
	@ApiOperation(value = "Método que registra novo débito")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
	public ResponseEntity<HttpStatus> registerNewDebt(@RequestBody DebtDTO debt) throws RegisterNotFoundException {
		return debtService.registerNewDebt(debt);
	}

	@GetMapping(value = "/all/{registerId}")
	@ApiOperation(value = "Método que retorna o nome com todos débitos por ID Registro")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
	public DebtValueDTO getAllDebtsById(@PathVariable Long registerId) throws DebtNotFoundException, RegisterNotFoundException {
		return debtService.getAllDebtsByRegister(registerId);
	}

	@GetMapping(value = "/fullDebt/{registerId}")
	@ApiOperation(value = "Método que retorna o valor total em débitos por ID Registo")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
	public DebtValueDTO getFullDebt(@PathVariable Long registerId) throws DebtNotFoundException {
		return debtService.getFullDebt(registerId);
	}

	@PutMapping(value = "/update")
	@ApiOperation(value = "Método que atualiza um débito")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
	public ResponseEntity<HttpStatus> updateValue(@RequestBody DebtDTO debtDTO) {
		return debtService.updateValue(debtDTO);
	}

	@PutMapping(value = "/pay")
	@ApiOperation(value = "Método utilizado para pagar um débito")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
	public ResponseEntity<HttpStatus> payValue(@RequestBody DebtDTO debtDTO) throws DebtNotFoundException {
		return debtService.payValue(debtDTO);//todo implementar métodos que trás todas pagas e todas devidas
	}

	@DeleteMapping(value = "/debtId/{debtId}")
	@ApiOperation(value = "Método que deleta um débito por ID debt")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	public ResponseEntity<HttpStatus> deleteDebt(@PathVariable Long debtId) throws DebtNotFoundException {
		return debtService.deleteDebtById(debtId);
	}

}
