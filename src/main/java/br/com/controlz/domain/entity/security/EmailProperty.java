package br.com.controlz.domain.entity.security;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "email_property")
public class EmailProperty implements Serializable {

	@ManyToOne
	@JoinColumn(name = "email_id", updatable = false, insertable = false)
	private Email email;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long emailPropertyId;

	@Column(name = "email_property_key")
	private String emailPropertyKey;

	@Column(name = "email_property_value")
	private String emailPropertyValue;

	@Column(name = "email_id")
	private Long emailId;

	public EmailProperty(Long emailPropertyId, String emailPropertyKey, String emailPropertyValue, Long emailId) {
		this.emailPropertyId = emailPropertyId;
		this.emailPropertyKey = emailPropertyKey;
		this.emailPropertyValue = emailPropertyValue;
		this.emailId = emailId;
	}

	public EmailProperty() {
	}

	public Email getEmail() {
		return email;
	}

	public void setEmail(Email email) {
		this.email = email;
	}

	public Long getEmailPropertyId() {
		return emailPropertyId;
	}

	public void setEmailPropertyId(Long emailPropertyId) {
		this.emailPropertyId = emailPropertyId;
	}

	public String getEmailPropertyKey() {
		return emailPropertyKey;
	}

	public void setEmailPropertyKey(String emailPropertyKey) {
		this.emailPropertyKey = emailPropertyKey;
	}

	public String getEmailPropertyValue() {
		return emailPropertyValue;
	}

	public void setEmailPropertyValue(String emailPropertyValue) {
		this.emailPropertyValue = emailPropertyValue;
	}

	public Long getEmailId() {
		return emailId;
	}

	public void setEmailId(Long emailId) {
		this.emailId = emailId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		EmailProperty that = (EmailProperty) o;
		return Objects.equals(email, that.email)
				&& Objects.equals(emailPropertyId, that.emailPropertyId)
				&& Objects.equals(emailPropertyKey, that.emailPropertyKey)
				&& Objects.equals(emailPropertyValue, that.emailPropertyValue)
				&& Objects.equals(emailId, that.emailId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, emailPropertyId, emailPropertyKey, emailPropertyValue, emailId);
	}

	@Override
	public String toString() {
		return "MailProperty{" + "mailPropertyId=" + emailPropertyId +
				", key='" + emailPropertyKey + '\'' +
				", value='" + emailPropertyValue + '\'' +
				", emailId='" + emailId + '\'' +
				'}';
	}
}
