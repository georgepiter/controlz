package br.com.controlz.service.job;

import br.com.controlz.domain.Constant;
import br.com.controlz.domain.entity.security.Email;
import br.com.controlz.domain.entity.security.EmailProperty;
import br.com.controlz.domain.exception.EmailException;
import br.com.controlz.domain.exception.EmailSenderException;
import br.com.controlz.domain.repository.EmailPropertyRepository;
import br.com.controlz.domain.repository.EmailRepository;
import br.com.controlz.service.MailBuildService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import sibModel.SendSmtpEmailTo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class JobResendEmailWithError {

	private static final Logger logger = LoggerFactory.getLogger(JobResendEmailWithError.class);

	private final EmailRepository emailRepository;
	private final MailBuildService mailBuildService;
	private final EmailPropertyRepository emailPropertyRepository;

	public JobResendEmailWithError(EmailRepository emailRepository,
								   MailBuildService mailBuildService,
								   EmailPropertyRepository emailPropertyRepository) {
		this.emailRepository = emailRepository;
		this.mailBuildService = mailBuildService;
		this.emailPropertyRepository = emailPropertyRepository;
	}

	@Scheduled(cron = "0 1 * * * *")
	private void resendEmailWithError() throws EmailSenderException {
		logger.info("Executando Job de reenvio de emails com erros");
		List<Email> emailErrorList = emailRepository.findAll().stream()
				.filter(email -> email.getEmailStatus().equals(Constant.EMAIL_SEND_ERROR)).toList();
		if (!emailErrorList.isEmpty()) {
			try {
				for (Email email : emailErrorList) {
					List<SendSmtpEmailTo> sendSmtpEmailTos = mailBuildService.compileEmail(email.getEmail());
					Map<String, String> bodyMsg = getEmailProperty(email);
					mailBuildService.sendCompiledEmail(sendSmtpEmailTos, email.getIdTemplate(), bodyMsg, email.getSubject());
				}
			} catch (EmailException e) {
				throw new EmailSenderException(e.getMessage());
			}
			logger.info("Job de reenvio de emails com erros finalizado");
		}
	}

	private Map<String, String> getEmailProperty(Email email) {
		List<EmailProperty> emailProperty = emailPropertyRepository.findAll()
				.stream().filter(p -> email.getIdMail().equals(p.getIdEmail())).toList();
		Map<String, String> bodyMessage = new HashMap<>();
		if (!emailProperty.isEmpty()) {
			emailProperty.forEach(
					property -> bodyMessage.put(property.getEmailPropertyKey(), property.getEmailPropertyValue())
			);
		}
		return bodyMessage;
	}
}
