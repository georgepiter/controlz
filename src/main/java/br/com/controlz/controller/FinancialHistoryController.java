package br.com.controlz.controller;

import br.com.controlz.domain.dto.FinancialHistoryDTO;
import br.com.controlz.domain.dto.HistoryDTO;
import br.com.controlz.domain.dto.ResponseEntityCustom;
import br.com.controlz.domain.exception.FinancialHistoryNotFoundException;
import br.com.controlz.service.FinancialHistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "Historico", produces = MediaType.APPLICATION_JSON_VALUE, tags = {"Historico"})
@RequestMapping(value = "api/v1/history")
public class FinancialHistoryController {

	private final FinancialHistoryService financialHistoryService;

	public FinancialHistoryController(FinancialHistoryService financialHistoryService) {
		this.financialHistoryService = financialHistoryService;
	}

	@GetMapping(value = "/{registerId}/{month}/{year}")
	@ApiOperation(value = "Método que retorna o histórico com o valor total dos débitos e créditos por mês/ano")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
	public FinancialHistoryDTO getHistoryByMonthAndYear(@PathVariable Long registerId,
														@PathVariable Integer month,
														@PathVariable Integer year) throws FinancialHistoryNotFoundException {
		return financialHistoryService.getHistoryByMonthAndYear(registerId, month, year);
	}

	@GetMapping(value = "/all/{registerId}")
	@ApiOperation(value = "Método que retorna todo o histórico do user por id")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
	public HistoryDTO getAllFinancialHistoryById(@PathVariable Long registerId) {
		return financialHistoryService.getAllFinancialHistoryById(registerId);
	}

	@DeleteMapping(value = "/{registerId}/{historyId}")
	@ApiOperation(value = "Deleta o histórico pelo id")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	public ResponseEntityCustom deleteHistoryById(@PathVariable Long registerId, @PathVariable Long historyId) throws FinancialHistoryNotFoundException {
		return financialHistoryService.deleteHistoryById(registerId, historyId);
	}
}
