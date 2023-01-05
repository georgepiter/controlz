package br.com.controlz.domain.entity.security;

import jakarta.persistence.*;

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
	private Long idMailProperty;

	@Column(name = "email_property_key")
	private String emailPropertyKey;

	@Column(name = "email_property_value")
	private String emailPropertyValue;

	@Column(name = "email_id")
	private Long idEmail;

	public EmailProperty(Long idMailProperty, String emailPropertyKey, String emailPropertyValue, Long idEmail) {
		this.idMailProperty = idMailProperty;
		this.emailPropertyKey = emailPropertyKey;
		this.emailPropertyValue = emailPropertyValue;
		this.idEmail = idEmail;
	}

	public EmailProperty() {
	}

	public Email getEmail() {
		return email;
	}

	public void setEmail(Email email) {
		this.email = email;
	}

	public Long getIdMailProperty() {
		return idMailProperty;
	}

	public void setIdMailProperty(Long idMailProperty) {
		this.idMailProperty = idMailProperty;
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

	public Long getIdEmail() {
		return idEmail;
	}

	public void setIdEmail(Long idEmail) {
		this.idEmail = idEmail;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		EmailProperty that = (EmailProperty) o;
		return Objects.equals(email, that.email)
				&& Objects.equals(idMailProperty, that.idMailProperty)
				&& Objects.equals(emailPropertyKey, that.emailPropertyKey)
				&& Objects.equals(emailPropertyValue, that.emailPropertyValue)
				&& Objects.equals(idEmail, that.idEmail);
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, idMailProperty, emailPropertyKey, emailPropertyValue, idEmail);
	}

	@Override
	public String toString() {
		final StringBuilder mailProperty = new StringBuilder("MailProperty{");
		mailProperty.append("idMailProperty=").append(idMailProperty);
		mailProperty.append(", key='").append(emailPropertyKey).append('\'');
		mailProperty.append(", value='").append(emailPropertyValue).append('\'');
		mailProperty.append(", idPropertyEmail='").append(idEmail).append('\'');
		mailProperty.append('}');
		return mailProperty.toString();
	}
}
