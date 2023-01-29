package br.com.controlz.service;

import br.com.controlz.domain.dto.FinancialHistoryDTO;
import br.com.controlz.domain.dto.HistoryDTO;
import br.com.controlz.domain.dto.ResponseEntityCustom;
import br.com.controlz.domain.entity.FinancialHistory;
import br.com.controlz.domain.exception.FinancialHistoryNotFoundException;
import br.com.controlz.domain.repository.FinancialHistoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FinancialHistoryService {

	private final FinancialHistoryRepository financialHistoryRepository;

	public FinancialHistoryService(FinancialHistoryRepository financialHistoryRepository) {
		this.financialHistoryRepository = financialHistoryRepository;
	}

	public FinancialHistoryDTO getHistoryByMonthAndYear(Long registerId, Integer month, Integer year) throws FinancialHistoryNotFoundException {
		String period = String.valueOf(year + "-" + String.format("%02d", month));
		Optional<FinancialHistory> financialHistory = financialHistoryRepository.findByRegisterIdAndPeriod(registerId, period);
		if (financialHistory.isEmpty()) {
			throw new FinancialHistoryNotFoundException("Historico para a data escolhida não localizado na base");
		}
		return new FinancialHistoryDTO.Builder()
				.registerId(financialHistory.get().getRegisterId())
				.financialHistoryId(financialHistory.get().getFinancialHistoryId())
				.totalCredit(financialHistory.get().getTotalCredit())
				.totalDebt(financialHistory.get().getTotalDebt())
				.balanceCredit(financialHistory.get().getBalanceCredit())
				.period(financialHistory.get().getPeriod())
				.createNewFinancialHistoryDTO();
	}

	public HistoryDTO getAllFinancialHistoryById(Long registerId) {
		HistoryDTO historyDTO = new HistoryDTO();
		List<FinancialHistory> financialHistory = financialHistoryRepository.findByRegisterId(registerId);
		List<FinancialHistoryDTO> newFinancialHistoryDTO = financialHistory.stream()
				.map(history -> new FinancialHistoryDTO.Builder()
						.financialHistoryId(history.getFinancialHistoryId())
						.registerId(history.getRegisterId())
						.balanceCredit(history.getBalanceCredit())
						.totalCredit(history.getTotalCredit())
						.totalDebt(history.getTotalDebt())
						.period(history.getPeriod())
						.createNewFinancialHistoryDTO())
				.toList();
		historyDTO.setFinancialList(newFinancialHistoryDTO);
		return historyDTO;
	}

	public ResponseEntityCustom deleteHistoryById(Long registerId, Long historyId) throws FinancialHistoryNotFoundException {
		Optional<FinancialHistory> financialHistory = financialHistoryRepository.findByRegisterIdAndFinancialHistoryId(registerId, historyId);
		if (financialHistory.isEmpty()) {
			throw new FinancialHistoryNotFoundException("Historico não encontrado pelo Id");
		}
		financialHistoryRepository.delete(financialHistory.get());
		return new ResponseEntityCustom(HttpStatus.NO_CONTENT.value(), HttpStatus.NO_CONTENT, "Historico deletado com sucesso!");
	}
}
