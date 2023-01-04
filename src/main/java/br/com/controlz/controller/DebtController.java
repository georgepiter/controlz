package br.com.controlz.controller;

import br.com.controlz.domain.dto.DebtDTO;
import br.com.controlz.domain.dto.DebtValueDTO;
import br.com.controlz.domain.exception.DebtNotFoundException;
import br.com.controlz.domain.exception.RegisterNotFoundException;
import br.com.controlz.service.DebtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/v1/debt")
public class DebtController {

	private final DebtService debtService;

	public DebtController(DebtService debtService) {
		this.debtService = debtService;
	}

	@PostMapping(value = "/")
	public ResponseEntity<HttpStatus> registerNewDebt(@RequestBody DebtDTO debt) throws RegisterNotFoundException {
		return debtService.registerNewDebt(debt);
	}

	@GetMapping(value = "/all/{registerId}")
	public DebtValueDTO getAllDebtsById(@PathVariable Long registerId) throws DebtNotFoundException, RegisterNotFoundException {
		return debtService.getAllDebtsByRegister(registerId);
	}

	@GetMapping(value = "/fullDebt/{registerId}")
	public DebtValueDTO getFullDebt(@PathVariable Long registerId) throws DebtNotFoundException {
		return debtService.getFullDebt(registerId);
	}

	@PutMapping(value = "/update")
	public ResponseEntity<HttpStatus> updateValue(@RequestBody DebtDTO debtDTO) {
		return debtService.updateValue(debtDTO);
	}

	@DeleteMapping(value = "/debtId/{debtId}")
	public ResponseEntity<HttpStatus> deleteDebt(@PathVariable Long debtId) throws DebtNotFoundException {
		return debtService.deleteDebtById(debtId);
	}

}
