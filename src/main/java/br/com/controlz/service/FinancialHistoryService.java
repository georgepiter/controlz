package br.com.controlz.service;

import br.com.controlz.domain.dto.FinancialHistoryDTO;
import br.com.controlz.domain.dto.HistoryDTO;
import br.com.controlz.domain.dto.ResponseEntityCustom;
import br.com.controlz.domain.entity.FinancialHistory;
import br.com.controlz.domain.exception.FinancialHistoryNotFoundException;
import br.com.controlz.domain.repository.FinancialHistoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import springfox.documentation.annotations.Cacheable;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

@Service
public class FinancialHistoryService {

	private final FinancialHistoryRepository financialHistoryRepository;

	public FinancialHistoryService(FinancialHistoryRepository financialHistoryRepository) {
		this.financialHistoryRepository = financialHistoryRepository;
	}

	@Cacheable(value = "cached")
	public HistoryDTO getAllFinancialHistoryById(Long registerId) {
		HistoryDTO historyDTO = new HistoryDTO();
		List<FinancialHistory> financialHistory = financialHistoryRepository.findByRegisterId(registerId);

		List<FinancialHistoryDTO> newFinancialHistoryDTO = financialHistory.stream()
				.sorted(Comparator.comparing(FinancialHistory::getPeriod).reversed())
				.map(history -> new FinancialHistoryDTO.Builder()
						.financialHistoryId(history.getFinancialHistoryId())
						.registerId(history.getRegisterId())
						.balanceCredit(history.getBalanceCredit())
						.totalCredit(history.getTotalCredit())
						.totalDebt(history.getTotalDebt())
						.period(formatHistoryDate(history.getPeriod()))
						.createNewFinancialHistoryDTO())
				.toList();

		historyDTO.setFinancialList(newFinancialHistoryDTO);
		return historyDTO;
	}

	private String formatHistoryDate(String period) {
		return YearMonth.parse(period, DateTimeFormatter.ofPattern("yyyy-MM"))
				.atEndOfMonth().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}

	public ResponseEntityCustom deleteHistoryById(Long registerId, Long historyId) throws FinancialHistoryNotFoundException {
		FinancialHistory financialHistory = financialHistoryRepository.findByRegisterIdAndFinancialHistoryId(registerId, historyId)
				.orElseThrow(() -> new FinancialHistoryNotFoundException("Historico não encontrado pelo Id"));

		financialHistoryRepository.delete(financialHistory);
		return new ResponseEntityCustom(HttpStatus.NO_CONTENT.value(), HttpStatus.NO_CONTENT, "Historico deletado com sucesso!");
	}
}
