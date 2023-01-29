package br.com.controlz.controller;

import br.com.controlz.domain.dto.DebtDTO;
import br.com.controlz.domain.dto.DebtValueDTO;
import br.com.controlz.domain.dto.ResponseEntityCustom;
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

import javax.validation.constraints.NotNull;

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
	public ResponseEntityCustom registerNewDebt(@NotNull @RequestBody DebtDTO debt) throws RegisterNotFoundException {
		return debtService.registerNewDebt(debt);
	}

	@GetMapping(value = "/all/{userId}/{registerId}")
	@ApiOperation(value = "Método que retorna todos os débitos do mês")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
	public DebtValueDTO getAllDebtsById(@PathVariable Long userId, @PathVariable Long registerId) throws RegisterNotFoundException {
		return debtService.getAllDebtsByRegister(registerId, userId);
	}

	@GetMapping(value = "/allPay/{userId}/{registerId}")
	@ApiOperation(value = "Método que retorna todos débitos pagos do mês")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
	public DebtValueDTO getAllDebtsPay(@PathVariable Long registerId, @PathVariable Long userId) throws DebtNotFoundException, RegisterNotFoundException {
		return debtService.getAllDebtsByStatusAndRegister(true, registerId, userId);
	}

	@GetMapping(value = "/allDue/{userId}/{registerId}")
	@ApiOperation(value = "Método que retorna todos débitos á pagar do mês")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
	public DebtValueDTO getAllDebtsDue(@PathVariable Long registerId, @PathVariable Long userId) throws DebtNotFoundException, RegisterNotFoundException {
		return debtService.getAllDebtsByStatusAndRegister(false, registerId, userId);
	}

	@PutMapping(value = "/update")
	@ApiOperation(value = "Método que atualiza um débito")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
	public ResponseEntity<HttpStatus> updateValue(@NotNull @RequestBody DebtDTO debtDTO) {
		return debtService.updateValue(debtDTO);
	}

	@PutMapping(value = "/pay")
	@ApiOperation(value = "Método utilizado para pagar um débito")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
	public ResponseEntity<HttpStatus> payValue(@RequestBody DebtDTO debtDTO) throws DebtNotFoundException {
		return debtService.payValue(debtDTO);
	}

	@DeleteMapping(value = "/{debtId}")
	@ApiOperation(value = "Método que deleta um débito")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	public ResponseEntityCustom deleteDebt(@PathVariable Long debtId) throws DebtNotFoundException {
		return debtService.deleteDebtById(debtId);
	}

}
