package br.com.controlz.service;

import br.com.controlz.domain.entity.Debt;
import br.com.controlz.domain.entity.security.Email;
import br.com.controlz.domain.entity.security.EmailProperty;
import br.com.controlz.domain.entity.security.User;
import br.com.controlz.domain.exception.EmailException;
import br.com.controlz.domain.exception.EmailNotFoundException;
import br.com.controlz.domain.repository.EmailPropertyRepository;
import br.com.controlz.domain.repository.EmailRepository;
import br.com.controlz.domain.response.EmailStatusResponse;
import br.com.controlz.utils.MoneyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sibModel.SendSmtpEmail;
import sibModel.SendSmtpEmailSender;
import sibModel.SendSmtpEmailTo;

import java.util.*;

import static br.com.controlz.domain.Constant.*;

@Service
public class MailBuildService {

	private static final Logger logger = LoggerFactory.getLogger(MailBuildService.class);

	private final APIService apiService;
	private final EmailRepository emailRepository;
	private final EmailPropertyRepository emailPropertyRepository;


	public MailBuildService(APIService apiService,
							EmailRepository emailRepository,
							EmailPropertyRepository emailPropertyRepository) {
		this.apiService = apiService;
		this.emailRepository = emailRepository;
		this.emailPropertyRepository = emailPropertyRepository;
	}


	public void newSendPasswordEmail(User user, String newPassword) throws UsernameNotFoundException, EmailException {
		List<SendSmtpEmailTo> listEmail = compileEmail(user.getEmail());
		Map<String, String> bodyMessage = buildMessageNewPassword(user.getName(), newPassword);
		sendCompiledEmail(listEmail, TEMPLATE_ID_NEW_PASSWORD, bodyMessage, NEW_PASSWORD);
	}

	public void createEmailDebt(Debt debt, String email) {
		List<SendSmtpEmailTo> listEmail = compileEmail(email);
		Map<String, String> bodyMessage = buildMessageDebt(debt);
		try {
			sendCompiledEmail(listEmail, TEMPLATE_ID_DEBT, bodyMessage, DUE_DATE_WARNING);
		} catch (EmailException e) {
			//ignored
		}
	}

	private Map<String, String> buildMessageDebt(Debt debt) {
		Map<String, String> bodyMessage = new HashMap<>();
		bodyMessage.put(VALUE, MoneyUtils.formatMoneyToPatternBR(debt.getValue()));
		bodyMessage.put(DUE_DATE, String.valueOf(debt.getDueDate()));
		bodyMessage.put(DESCRIPTION, debt.getDebtDescription());
		return bodyMessage;
	}

	protected Map<String, String> buildMessageNewPassword(String name, String newPassword) {
		Map<String, String> bodyMessage = new HashMap<>();
		bodyMessage.put(PASSWORD, newPassword);
		bodyMessage.put(NAME, name);
		return bodyMessage;
	}

	public List<SendSmtpEmailTo> compileEmail(String studentEmail) {
		List<SendSmtpEmailTo> listEmail = new ArrayList<>();
		SendSmtpEmailTo email = new SendSmtpEmailTo();
		email.setEmail(studentEmail);
		listEmail.add(email);
		return listEmail;
	}

	public void sendCompiledEmail(List<SendSmtpEmailTo> listEmail, Long templateId, Map<String, String> bodyMsg, String subject) throws EmailException {
		SendSmtpEmailSender sender = new SendSmtpEmailSender();
		sender.setName(EMAIL_SENDER);
		sender.setName("George Piter");
		sender.setEmail("george.piter@terra.com.br");
		SendSmtpEmail sendSmtpEmail = new SendSmtpEmail();
		sendSmtpEmail.setSender(sender);
		sendSmtpEmail.setTo(listEmail);
		sendSmtpEmail.setSubject(subject);
		sendSmtpEmail.setTemplateId(templateId);
		sendSmtpEmail.setParams(bodyMsg);
		try {
			apiService.sendMail(sendSmtpEmail);
			if (validateStatusEmail(sendSmtpEmail.getTo().get(0).getEmail())) {
				saveObject(sendSmtpEmail, bodyMsg);
				logger.info("E-mail enviado com sucesso!");
			}
		} catch (Exception e) {
			throw new EmailException(e.getMessage());
		}
	}

	private void saveObject(SendSmtpEmail sendSmtpEmail, Map<String, String> bodyMsg) {
		String email = sendSmtpEmail.getTo().get(0).getEmail();
		Email mail = new Email();
		mail.setEmail(email);
		mail.setSubject(sendSmtpEmail.getSubject());
		mail.setIdTemplate(sendSmtpEmail.getTemplateId());
		mail.setEmailStatus(EMAIL_SEND_TRUE);
		emailRepository.save(mail);
		getMessageBodyToProperty(mail, bodyMsg);
		logger.info("E-mail salvo no banco de dados com sucesso!");
	}

	private void getMessageBodyToProperty(Email email, Map<String, String> bodyMsg) {
		bodyMsg.forEach((key, value) -> {
			EmailProperty emailProperty = new EmailProperty();
			emailProperty.setEmailPropertyKey(key);
			emailProperty.setEmailPropertyValue(value);
			emailProperty.setIdEmail(email.getIdMail());
			emailPropertyRepository.save(emailProperty);
		});
	}

	public void validateEmailStatus(EmailStatusResponse emailStatusResponse) {
		String event = emailStatusResponse.getEvent().toLowerCase(Locale.ROOT);
		try {
			if (!event.isBlank() && event.contains(EMAIL_DELIVERED)) {
				validateEmailMessageSuccessResponse(emailStatusResponse);
			} else if (validateEventStatus(event)) {
				validateEmailMessageErrorResponse(emailStatusResponse);
			} else {
				logger.warn("Evento de email não esperado");
			}
		} catch (EmailNotFoundException e) {
			logger.warn(e.getMessage());
		}
	}

	private boolean validateEventStatus(String event) {
		return event.contains(EMAIL_ERROR)
				|| event.contains(EMAIL_SOFT_BOUNCE)
				|| event.contains(EMAIL_HARD_BOUNCE)
				|| event.contains(EMAIL_INVALID)
				|| event.contains(EMAIL_DEFERRED)
				|| event.contains(EMAIL_COMPLAINT);
	}

	private void validateEmailMessageSuccessResponse(EmailStatusResponse emailStatusResponse) throws EmailNotFoundException {
		Optional<Email> emailByStatusSent = emailRepository.findByEmail(emailStatusResponse.getEmail());
		Email email = verifyMail(emailByStatusSent);
		emailRepository.deleteById(email.getIdMail());
		logger.info("E-mail entregue com sucesso");
	}

	private void validateEmailMessageErrorResponse(EmailStatusResponse emailStatusResponse) throws EmailNotFoundException {
		Optional<Email> emailByStatusSend = emailRepository.findByEmail(emailStatusResponse.getEmail());
		Email email = verifyMail(emailByStatusSend);
		if (EMAIL_SEND_ERROR.equals(email.getEmailStatus())) {
			emailRepository.deleteById(email.getIdMail());
		} else {
			email.setEmailStatus(EMAIL_SEND_ERROR);
			emailRepository.save(email);
			logger.info("E-mail com status de erro salvo");
		}
	}

	private Email verifyMail(Optional<Email> emailByStatusSend) throws EmailNotFoundException {
		if (emailByStatusSend.isEmpty()) {
			throw new EmailNotFoundException("email não encontrado na base");
		}
		return emailByStatusSend.get();
	}

	private boolean validateStatusEmail(String email) {
		boolean status = false;
		Optional<Email> emailStatus = emailRepository.findByEmailAndEmailStatus(email, EMAIL_SEND_ERROR);
		if (emailStatus.isEmpty()) {
			status = true;
		}
		return status;
	}
}

