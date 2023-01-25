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
	private Long emailId;

	@Column(name = "email")
	private String email;

	@Column(name = "email_status")
	private Integer emailStatus;

	@Column(name = "subject")
	private String subject;

	@Column(name = "id_template")
	private Long templateId;

	public Email(Long emailId, String email, Integer emailStatus,
				 String subject, Long templateId) {
		this.emailId = emailId;
		this.email = email;
		this.emailStatus = emailStatus;
		this.subject = subject;
		this.templateId = templateId;
	}

	public Email() {
	}

	public List<EmailProperty> getProperties() {
		return properties;
	}

	public Long getEmailId() {
		return emailId;
	}

	public void setEmailId(Long emailId) {
		this.emailId = emailId;
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

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Email email = (Email) o;
		return Objects.equals(properties, email.properties)
				&& Objects.equals(emailId, email.emailId)
				&& Objects.equals(this.email, email.email)
				&& Objects.equals(emailStatus, email.emailStatus)
				&& Objects.equals(subject, email.subject)
				&& Objects.equals(templateId, email.templateId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(properties, emailId, email, emailStatus, subject, templateId);
	}

	@Override
	public String toString() {
		return "Mail{" + "idMail=" + emailId +
				", email='" + this.email + '\'' +
				", emailStatus=" + emailStatus +
				", subject='" + subject + '\'' +
				", id_template=" + templateId +
				'}';
	}
}
