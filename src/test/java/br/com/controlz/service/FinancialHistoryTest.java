package br.com.controlz.service;

import br.com.controlz.domain.dto.FinancialHistoryDTO;
import br.com.controlz.domain.dto.HistoryDTO;
import br.com.controlz.domain.dto.ResponseEntityCustom;
import br.com.controlz.domain.entity.FinancialHistory;
import br.com.controlz.domain.exception.FinancialHistoryNotFoundException;
import br.com.controlz.domain.repository.FinancialHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FinancialHistoryTest {


	@Mock
	private FinancialHistoryRepository financialHistoryRepository;

	@InjectMocks
	private FinancialHistoryService financialHistoryService;

	@BeforeEach
	public void setUp() {
		financialHistoryService = new FinancialHistoryService(this.financialHistoryRepository);
	}

	@Test
	@DisplayName("Deve retornar histórico financeiro vazio para um ID de registro não encontrado")
	void shouldReturnEmptyFinancialHistoryForNotFoundRegisterId() {
		// given
		Long registerId = 1L;
		when(financialHistoryRepository.findByRegisterId(registerId)).thenReturn(Collections.emptyList());

		// when
		HistoryDTO historyDTO = financialHistoryService.getAllFinancialHistoryById(registerId);

		// then
		assertNotNull(historyDTO);
		assertTrue(historyDTO.getFinancialList().isEmpty());
	}

	@Test
	@DisplayName("Deve retornar histórico financeiro ordenado por período decrescente")
	void shouldReturnFinancialHistorySortedByPeriodDesc() {
		// given
		Long registerId = 1L;
		List<FinancialHistory> financialHistory = new ArrayList<>();
		financialHistory.add(new FinancialHistory.Builder()
				.financialHistoryId(1L)
				.registerId(registerId)
				.balanceCredit(100.0d)
				.totalCredit(300.0d)
				.totalDebt(200.0d)
				.period("2023,02")
				.createNewFinancialHistory());

		financialHistory.add(new FinancialHistory.Builder()
				.financialHistoryId(2L)
				.registerId(registerId)
				.balanceCredit(100.0d)
				.totalCredit(300.0d)
				.totalDebt(200.0d)
				.period("2023,01")
				.createNewFinancialHistory());

		when(financialHistoryRepository.findByRegisterId(registerId)).thenReturn(financialHistory);

		// when
		HistoryDTO historyDTO = financialHistoryService.getAllFinancialHistoryById(registerId);

		// then
		assertNotNull(historyDTO);
		List<FinancialHistoryDTO> financialList = historyDTO.getFinancialList();
		assertFalse(financialList.isEmpty());
		assertEquals(2, financialList.size());
		assertEquals(1L, financialList.get(0).getFinancialHistoryId());
		assertEquals(2L, financialList.get(1).getFinancialHistoryId());
	}

	@Test
	@DisplayName("Deve retornar DTO do histórico financeiro com valores corretos")
	void shouldReturnFinancialHistoryDTOWithCorrectValues() {
		// given
		Long registerId = 1L;
		LocalDate period = LocalDate.of(2022, 1, 1);

		FinancialHistory financialHistory = new FinancialHistory.Builder()
				.financialHistoryId(1L)
				.registerId(registerId)
				.balanceCredit(100.0d)
				.totalCredit(200.0d)
				.totalDebt(50.0d)
				.period(String.valueOf(period))
				.createNewFinancialHistory();

		when(financialHistoryRepository.findByRegisterId(registerId)).thenReturn(Collections.singletonList(financialHistory));

		// when
		HistoryDTO historyDTO = financialHistoryService.getAllFinancialHistoryById(registerId);

		// then
		assertNotNull(historyDTO);

		List<FinancialHistoryDTO> financialList = historyDTO.getFinancialList();
		assertFalse(financialList.isEmpty());
		assertEquals(1, financialList.size());

		FinancialHistoryDTO financialHistoryDTO = financialList.get(0);
		assertEquals(1L, financialHistoryDTO.getFinancialHistoryId());
		assertEquals(registerId, financialHistoryDTO.getRegisterId());
		assertEquals(100.0, financialHistoryDTO.getBalanceCredit(), 0.01);
		assertEquals(200.0, financialHistoryDTO.getTotalCredit(), 0.01);
		assertEquals(50.0, financialHistoryDTO.getTotalDebt(), 0.01);
		assertEquals(String.valueOf(period), financialHistoryDTO.getPeriod());
	}

	@Test
	@DisplayName("Deve deletar histórico e retornar status 204")
	void shouldDeleteHistoryAndReturn204() throws FinancialHistoryNotFoundException {
		// given
		Long registerId = 1L;
		Long historyId = 1L;
		FinancialHistory financialHistory = new FinancialHistory.Builder()
				.financialHistoryId(historyId)
				.registerId(registerId)
				.balanceCredit(100.0)
				.totalCredit(150.0)
				.totalDebt(50.0)
				.period("202201")
				.createNewFinancialHistory();

		when(financialHistoryRepository.findByRegisterIdAndFinancialHistoryId(registerId, historyId)).thenReturn(Optional.of(financialHistory));

		// when
		ResponseEntityCustom response = financialHistoryService.deleteHistoryById(registerId, historyId);

		// then
		assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
		assertEquals(HttpStatus.NO_CONTENT, response.getError());
		assertEquals("Historico deletado com sucesso!", response.getMessage());

		ArgumentCaptor<FinancialHistory> financialHistoryCaptor = ArgumentCaptor.forClass(FinancialHistory.class);
		verify(financialHistoryRepository).delete(financialHistoryCaptor.capture());
		assertEquals(financialHistory, financialHistoryCaptor.getValue());
	}

}
