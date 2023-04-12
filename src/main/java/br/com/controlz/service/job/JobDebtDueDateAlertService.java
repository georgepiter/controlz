package br.com.controlz.service.job;

import br.com.controlz.domain.entity.Debt;
import br.com.controlz.domain.entity.Register;
import br.com.controlz.domain.entity.security.Email;
import br.com.controlz.domain.entity.security.User;
import br.com.controlz.domain.enums.StatusEnum;
import br.com.controlz.domain.repository.DebtRepository;
import br.com.controlz.domain.repository.EmailRepository;
import br.com.controlz.domain.repository.RegisterRepository;
import br.com.controlz.domain.repository.UserRepository;
import br.com.controlz.service.MailBuildService;
import br.com.controlz.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class JobDebtDueDateAlertService {

	private static final Logger logger = LoggerFactory.getLogger(JobDebtDueDateAlertService.class);

	private final DebtRepository debtRepository;
	private final MailBuildService mailBuildService;
	private final RegisterRepository registerRepository;
	private final EmailRepository emailRepository;
	private final UserRepository userRepository;

	public JobDebtDueDateAlertService(DebtRepository debtRepository,
									  MailBuildService mailBuildService,
									  RegisterRepository registerRepository,
									  EmailRepository emailRepository,
									  UserRepository userRepository) {
		this.debtRepository = debtRepository;
		this.mailBuildService = mailBuildService;
		this.registerRepository = registerRepository;
		this.emailRepository = emailRepository;
		this.userRepository = userRepository;
	}

	@Scheduled(cron = "00 0 0 * * *")
	private void checkDebtDueDate() {
		logger.info("Executando Job de alerta de vencimento das dívidas");
		List<Debt> debts = debtRepository.findByStatus(StatusEnum.AWAITING_PAYMENT.getValue());
		if (debts.isEmpty()) {
			logger.info("Não foram encontradas dívidas á pagar");
		}

		List<Debt> dueDebts = new ArrayList<>();
		debts.forEach(
				debt -> {
					boolean isDaysBeforeDueDate = DateUtils.checkDaysBeforeExpiration(debt.getDueDate());
					if (isDaysBeforeDueDate) {
						dueDebts.add(debt);
					}
				}
		);

		dueDebts.forEach(
				debt -> {
					Long idRegister = debt.getRegisterId();
					Optional<Register> register = registerRepository.findById(idRegister);
					Optional<User> user = userRepository.findById(register.get().getUserId());
					if (emailSent(register, user.get().getEmail())) {
						register.ifPresent(regis -> mailBuildService.createEmailDebt(debt, user.get().getEmail()));
					}
				}
		);
	}

	private boolean emailSent(Optional<Register> register, String email) {
		boolean emailStatus = true;
		if (register.isPresent()) {
			Optional<Email> registerEmail = emailRepository.findByEmail(email);
			emailStatus = registerEmail.isEmpty();
		}
		return emailStatus;
	}
}
