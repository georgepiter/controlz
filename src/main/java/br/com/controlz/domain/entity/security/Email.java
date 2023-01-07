package br.com.controlz.domain.entity.security;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "email")
public class Email implements Serializable {

	@OneToMany(mappedBy = "email", cascade = CascadeType.REMOVE)
	private final List<EmailProperty> properties = new ArrayList<>();

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long idMail;

	@Column(name = "email")
	private String email;

	@Column(name = "email_status")
	private Integer emailStatus;

	@Column(name = "subject")
	private String subject;

	@Column(name = "id_template")
	private Long idTemplate;

	public Email(Long idMail, String email, Integer emailStatus,
				 String subject, Long idTemplate) {
		this.idMail = idMail;
		this.email = email;
		this.emailStatus = emailStatus;
		this.subject = subject;
		this.idTemplate = idTemplate;
	}

	public Email() {
	}

	public List<EmailProperty> getProperties() {
		return properties;
	}

	public List<EmailProperty> setProperties(List<EmailProperty> properties) {
		return this.properties;
	}

	public Long getIdMail() {
		return idMail;
	}

	public void setIdMail(Long idMail) {
		this.idMail = idMail;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getEmailStatus() {
		return emailStatus;
	}

	public void setEmailStatus(Integer emailStatus) {
		this.emailStatus = emailStatus;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Long getIdTemplate() {
		return idTemplate;
	}

	public void setIdTemplate(Long idTemplate) {
		this.idTemplate = idTemplate;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Email email = (Email) o;
		return Objects.equals(properties, email.properties)
				&& Objects.equals(idMail, email.idMail)
				&& Objects.equals(this.email, email.email)
				&& Objects.equals(emailStatus, email.emailStatus)
				&& Objects.equals(subject, email.subject)
				&& Objects.equals(idTemplate, email.idTemplate);
	}

	@Override
	public int hashCode() {
		return Objects.hash(properties, idMail, email, emailStatus, subject, idTemplate);
	}

	@Override
	public String toString() {
		final StringBuilder mail = new StringBuilder("Mail{");
		mail.append("idMail=").append(idMail);
		mail.append(", email='").append(this.email).append('\'');
		mail.append(", emailStatus=").append(emailStatus);
		mail.append(", subject='").append(subject).append('\'');
		mail.append(", id_template=").append(idTemplate);
		mail.append('}');
		return mail.toString();
	}
}
