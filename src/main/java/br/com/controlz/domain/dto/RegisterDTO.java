package br.com.controlz.domain.dto;

import java.time.LocalDate;

public class RegisterDTO {

	private final Long id;
	private String name;
	private String email;
	private String cell;
	private Double others;
	private Double salary;
	private LocalDate registrationDate;
	private byte[] photo;

	protected RegisterDTO(Long id, String name, String email, String cell, Double others, Double salary, LocalDate registrationDate, byte[] photo) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.cell = cell;
		this.others = others;
		this.salary = salary;
		this.registrationDate = registrationDate;
		this.photo = photo;
	}

	public static final class Builder {
		private Long id;
		private String name;
		private String email;
		private String cell;
		private Double others;
		private Double salary;
		private LocalDate registrationDate;
		private byte[] photo;

		public Builder() {
			//ignored
		}

		public Builder name(String val) {
			name = val;
			return this;
		}

		public Builder id(Long val) {
			id = val;
			return this;
		}

		public Builder registrationDate(LocalDate val) {
			registrationDate = val;
			return this;
		}

		public Builder email(String val) {
			email = val;
			return this;
		}

		public Builder cell(String val) {
			cell = val;
			return this;
		}

		public Builder others(Double val) {
			others = val;
			return this;
		}

		public Builder salary(Double val) {
			salary = val;
			return this;
		}

		public Builder photo(byte[] val) {
			photo = val;
			return this;
		}

		public RegisterDTO createNewRegisterDTO() {
			return new RegisterDTO(
					id, name, email, cell,
					others, salary, registrationDate, photo
			);
		}
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCell() {
		return cell;
	}

	public void setCell(String cell) {
		this.cell = cell;
	}

	public Double getOthers() {
		return others;
	}

	public void setOthers(Double others) {
		this.others = others;
	}

	public Double getSalary() {
		return salary;
	}

	public void setSalary(Double salary) {
		this.salary = salary;
	}

	public LocalDate getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(LocalDate registrationDate) {
		this.registrationDate = registrationDate;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}
}



