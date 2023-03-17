package br.com.controlz.service.job;

import br.com.controlz.domain.entity.Debt;
import br.com.controlz.domain.entity.FinancialHistory;
import br.com.controlz.domain.entity.Register;
import br.com.controlz.domain.repository.DebtRepository;
import br.com.controlz.domain.repository.FinancialHistoryRepository;
import br.com.controlz.domain.repository.RegisterRepository;
import br.com.controlz.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Component
public class JobCreateFinancialHistoryService {

	private static final Logger logger = LoggerFactory.getLogger(JobCreateFinancialHistoryService.class);

	private final DebtRepository debtRepository;
	private final RegisterRepository registerRepository;
	private final FinancialHistoryRepository financialHistoryRepository;

	public JobCreateFinancialHistoryService(DebtRepository debtRepository,
	                                        RegisterRepository registerRepository,
	                                        FinancialHistoryRepository financialHistoryRepository) {
		this.debtRepository = debtRepository;
		this.registerRepository = registerRepository;
		this.financialHistoryRepository = financialHistoryRepository;
	}

	@Scheduled(cron = "0 0 28-31 * *")
	private void createFinancialHistory() {
		logger.info("Executando job para criação do historico");
		List<Debt> debts = getAllDebtsForCurrentMonth();
		String dateFormatted = DateUtils.getPeriod();

		List<FinancialHistory> debtsHistory = getAllRegister().stream()
				.map(register -> {
					double totalCredit = register.getSalary() + register.getOthers();
					double totalDebt = calculateTotalDebt(debts, register.getRegisterId());
					return new FinancialHistory.Builder()
							.registerId(register.getRegisterId())
							.totalCredit(totalCredit)
							.totalDebt(totalDebt)
							.balanceCredit(totalCredit - totalDebt)
							.period(dateFormatted)
							.createNewFinancialHistory();
				}).toList();
		financialHistoryRepository.saveAll(debtsHistory);
	}

	private double calculateTotalDebt(List<Debt> debts, Long registerId) {
		return debts.stream()
				.filter(debt -> registerId.equals(debt.getRegisterId()))
				.mapToDouble(Debt::getValue)
				.sum();
	}

	private List<Register> getAllRegister() {
		return registerRepository.findAll();
	}

	private List<Debt> getAllDebtsForCurrentMonth() {
		return debtRepository.findByDueDate(getFirstDayOfCurrentMonth(), getLastDayOfCurrentMonth());
	}

	protected LocalDate getFirstDayOfCurrentMonth() {
		return LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
	}

	protected LocalDate getLastDayOfCurrentMonth() {
		return LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
	}

}
