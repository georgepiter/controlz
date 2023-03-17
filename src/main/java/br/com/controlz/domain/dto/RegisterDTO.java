package br.com.controlz.domain.dto;

import java.time.LocalDate;

public class RegisterDTO {

	private final Long registerId;
	private final Long userId;
	private String cell;
	private Double others;
	private Double salary;
	private LocalDate registrationDate;
	private byte[] photo;

	protected RegisterDTO(Long registerId, String cell, Double others, Double salary,
						  LocalDate registrationDate, byte[] photo, Long userId) {
		this.registerId = registerId;
		this.cell = cell;
		this.others = others;
		this.salary = salary;
		this.registrationDate = registrationDate;
		this.photo = photo;
		this.userId = userId;
	}

	public RegisterDTO() {
		this.registerId = null;
		this.userId = null;
		this.cell = "null";
		this.others = 0.00;
		this.salary = 0.00;
		this.registrationDate = LocalDate.now();
		this.photo = new byte[0];
	}

	public static final class Builder {
		private Long userId;
		private Long registerId;
		private Double salary;
		private String cell;
		private Double others;
		private LocalDate registrationDate;
		private byte[] photo;


		public Builder() {
			//ignored
		}

		public Builder registerId(Long val) {
			registerId = val;
			return this;
		}

		public Builder registrationDate(LocalDate val) {
			registrationDate = val;
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

		public Builder userId(Long val) {
			userId = val;
			return this;
		}

		public RegisterDTO createNewRegisterDTO() {
			return new RegisterDTO(
					registerId, cell,
					others, salary, registrationDate, photo, userId
			);
		}
	}

	public Long getRegisterId() {
		return registerId;
	}

	public String getCell() {
		return cell;
	}

	public Double getOthers() {
		return others;
	}

	public Double getSalary() {
		return salary;
	}

	public LocalDate getRegistrationDate() {
		return registrationDate;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public Long getUserId() {
		return userId;
	}
}



