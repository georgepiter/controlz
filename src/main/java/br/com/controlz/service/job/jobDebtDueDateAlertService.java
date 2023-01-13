package br.com.controlz.service.job;


import br.com.controlz.domain.entity.Debt;
import br.com.controlz.domain.enums.StatusEnum;
import br.com.controlz.domain.repository.DebtRepository;
import br.com.controlz.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class jobDebtDueDateAlertService {

	private static final Logger logger = LoggerFactory.getLogger(jobDebtDueDateAlertService.class);

	private final DebtRepository debtRepository;

	public jobDebtDueDateAlertService(DebtRepository debtRepository) {
		this.debtRepository = debtRepository;
	}


	@Scheduled(cron = "0 * * * * *")
	private void checkDebtDueDate() {
		logger.info("Executando Job de alerta de vencimento das dívidas");
		List<Debt> debts = debtRepository.findByStatus(StatusEnum.AWAITING_PAYMENT.getValue());
		if (debts.isEmpty()) {
			logger.info("Não foram encontradas dívidas á pagar");
		}
		LocalDate localDateNowSubtractingDay = DateUtils.localDateNowSubtractingDays(3);

		List<Debt> dueDebts = debts.stream().filter(debt -> debt.getDueDate().isBefore(localDateNowSubtractingDay)).toList();


		debts.forEach(
				debt -> {
					int teste = DateUtils.teste(debt.getDueDate(), localDateNowSubtractingDay);
				}
		);


//		System.out.println("dueDebts = " + dueDebts);

//				debt.getDueDate().isAfter(LocalDate.now())).toList();

//		for (Installment installment : allInstallments) {
//			if (isInstallmentDue(installment)) {
//				installment.setStatus(StatusEnum.DUE.getValue());
//				installmentRepository.save(installment);
//				logger.info("Foram encontradas parcelas vencidas!");
//			}
//		}
//		checkOverdueInstallments();
	}

//	private void checkOverdueInstallments() {
//		List<InstallmentDTO> installments = new ArrayList<>();
//		installmentRepository.findAll().forEach(installment -> {
//			if (installmentIsDueWithoutEmailSend(installment)) {
//				InstallmentDTO installmentDto = new InstallmentDTO();
//				installmentDto.setDueDate(installment.getDueDate());
//				installmentDto.setCpf(installment.getCpf());
//				installmentDto.setInstallmentNumber(installment.getInstallmentNumber());
//				installmentDto.setInstallmentValue(installment.getInstallmentValue());
//				installments.add(installmentDto);
//			}
//		});
//		if (!installments.isEmpty()) {
//			ResponseEntity<List<StudentDTO>> allStudents = studentService.findAllStudents();
//			installments.forEach(installment -> Objects.requireNonNull(allStudents.getBody())
//					.stream().filter(student -> student.getCpf().equals(installment.getCPF()))
//					.findFirst().ifPresent(student -> installment.setEmail(student.getEmail())));
//
//			mailBuildService.createEmailInstallmentDue(installments.stream().collect(groupingBy(InstallmentDTO::getCPF)));
//			chargeEmailSendStatus();
//		} else {
//			logger.info("Não foram encontradas parcelas vencidas");
//		}
//	}
//
//	private boolean installmentIsDueWithoutEmailSend(Installment installment) {
//		return installment.getStatus().equals(StatusEnum.DUE.getValue())
//				&& installment.getSendEmail().equals(Constant.EMAIL_NOT_SEND);
//	}
//
//	protected void chargeEmailSendStatus() {
//		List<Installment> installments = installmentRepository.findAll();
//		for (Installment installment : installments) {
//			if (isNotSentEmail(installment)) {
//				installment.setSendEmail(Constant.EMAIL_SEND);
//				installmentRepository.save(installment);
//				logger.info("Alteração de status de envio de email realizada");
//			}
//		}
//	}
//
//	private boolean isNotSentEmail(Installment installment) {
//		return installment.getStatus().equals(StatusEnum.DUE.getValue())
//				&& installment.getDueDate().isBefore(LocalDate.now())
//				&& installment.getSendEmail().equals(Constant.EMAIL_NOT_SEND);
//	}
//
//	private boolean isInstallmentDue(Installment installment) {
//		return installment.getStatus().equals(StatusEnum.VALID.getValue())
//				&& installment.getDueDate().isBefore(LocalDate.now())
//				&& installment.getSendEmail().equals(Constant.EMAIL_NOT_SEND);
//	}


}
