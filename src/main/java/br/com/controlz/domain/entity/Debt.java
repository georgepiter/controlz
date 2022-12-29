package br.com.controlz.domain.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "debt_control")
public class Debt implements Serializable {

	@ManyToOne
	@JoinColumn(name = "debt", updatable = false, insertable = false)
	private Register register;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "input_date")
	private LocalDate inputDate;

	@Column(name = "debt_description")
	private String debtDescription;

	@Column(name = "value")
	private Double value;

	protected Debt(LocalDate inputDate, String debtDescription, Double value) {
		this.inputDate = inputDate;
		this.debtDescription = debtDescription;
		this.value = value;
	}

	public static final class Builder {

		private LocalDate inputDate;
		private String debtDescription;
		private Double value;

		public Builder() {
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

		public Debt createDebt() {
			return new Debt(
					inputDate, debtDescription, value
			);
		}
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Debt debt = (Debt) o;
		return Objects.equals(id, debt.id) && Objects.equals(inputDate, debt.inputDate) && Objects.equals(debtDescription, debt.debtDescription) && Objects.equals(value, debt.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, inputDate, debtDescription, value);
	}

	@Override
	public String toString() {
		return "Debt{" + "inputDate=" + inputDate +
				", debtDescription='" + debtDescription + '\'' +
				", value=" + value +
				'}';
	}
}
