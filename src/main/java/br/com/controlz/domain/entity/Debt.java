package br.com.controlz.domain.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "debt_control")
public class Debt implements Serializable {

	@ManyToOne
	@JoinColumn(name = "register_id", updatable = false, insertable = false)
	private Register register;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long idDebt;

	@Column(name = "input_date")
	private LocalDate inputDate;

	@Column(name = "debt_description")
	private String debtDescription;

	@Column(name = "value")
	private Double value;

	@Column(name = "register_id")
	private Long idRegister;

	public Debt() {
	}

	protected Debt(Long idDebt, LocalDate inputDate, String debtDescription, Double value, Long idRegister) {
		this.idDebt = idDebt;
		this.inputDate = inputDate;
		this.debtDescription = debtDescription;
		this.value = value;
		this.idRegister = idRegister;
	}

	public static final class Builder {
		private Long idDebt;
		private LocalDate inputDate;
		private String debtDescription;
		private Double value;
		private Long idRegister;

		public Builder() {
			//ignored
		}

		public Builder idDebt(Long val) {
			idDebt = val;
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

		public Builder idRegister(Long val) {
			idRegister = val;
			return this;
		}

		public Debt createDebt() {
			return new Debt(
					idDebt,inputDate, debtDescription, value, idRegister
			);
		}
	}

	public void setIdDebt(Long idDebt) {
		this.idDebt = idDebt;
	}

	public Long getIdDebt() {
		return idDebt;
	}

	public Long getIdRegister() {
		return idRegister;
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

	public void setIdRegister(Long idRegister) {
		this.idRegister = idRegister;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Debt debt = (Debt) o;
		return Objects.equals(register, debt.register) && Objects.equals(idDebt, debt.idDebt) && Objects.equals(inputDate, debt.inputDate) && Objects.equals(debtDescription, debt.debtDescription) && Objects.equals(value, debt.value) && Objects.equals(idRegister, debt.idRegister);
	}

	@Override
	public int hashCode() {
		return Objects.hash(register, idDebt, inputDate, debtDescription, value, idRegister);
	}

	@Override
	public String toString() {
		return "Debt{" + "register=" + register +
				", id=" + idDebt +
				", inputDate=" + inputDate +
				", debtDescription='" + debtDescription + '\'' +
				", value=" + value +
				", idRegister=" + idRegister +
				'}';
	}
}
