package br.com.controlz.domain.dto;

import java.time.LocalDate;

public class DebtDTO {

	private Long idDebt;
	private Long idRegister;
	private String name;
	private LocalDate inputDate;
	private String debtDescription;
	private Double value;
	private String status;
	private LocalDate paymentDate;

	protected DebtDTO(Long idDebt, Long idRegister, String name, LocalDate inputDate,
					  String debtDescription, Double value, String status, LocalDate paymentDate) {
		this.idDebt = idDebt;
		this.idRegister = idRegister;
		this.name = name;
		this.inputDate = inputDate;
		this.debtDescription = debtDescription;
		this.value = value;
		this.status = status;
		this.paymentDate = paymentDate;
	}

	public static final class Builder {
		private Long idDebt;
		private Long idRegister;
		private String name;
		private LocalDate inputDate;
		private String debtDescription;
		private Double value;
		private String status;
		private LocalDate paymentDate;

		public Builder() {
			//ignored
		}

		public Builder idDebt(Long val) {
			idDebt = val;
			return this;
		}

		public Builder idRegister(Long val) {
			idRegister = val;
			return this;
		}

		public Builder name(String val) {
			name = val;
			return this;
		}

		public Builder inputDate(LocalDate val) {
			inputDate = val;
			return this;
		}

		public Builder debtDescription(String val) {
			debtDescription = val;
			return this;
		}

		public Builder value(Double val) {
			value = val;
			return this;
		}

		public Builder status(String val) {
			status = val;
			return this;
		}

		public Builder paymentDate(LocalDate val) {
			paymentDate = val;
			return this;
		}

		public DebtDTO createNewDebtDTO() {
			return new DebtDTO(
					idDebt, idRegister, name, inputDate, debtDescription, value, status, paymentDate
			);
		}
	}

	public Long getIdDebt() {
		return idDebt;
	}

	public void setIdDebt(Long idDebt) {
		this.idDebt = idDebt;
	}

	public Long getIdRegister() {
		return idRegister;
	}

	public void setIdRegister(Long idRegister) {
		this.idRegister = idRegister;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getInputDate() {
		return inputDate;
	}

	public void setInputDate(LocalDate inputDate) {
		this.inputDate = inputDate;
	}

	public String getDebtDescription() {
		return debtDescription;
	}

	public void setDebtDescription(String debtDescription) {
		this.debtDescription = debtDescription;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDate getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(LocalDate paymentDate) {
		this.paymentDate = paymentDate;
	}
}
