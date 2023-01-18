package br.com.controlz.domain.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.StringJoiner;

@Entity
@Table(name = "debt_control")
public class Debt implements Serializable {

	@ManyToOne
	@JoinColumn(name = "register_id", updatable = false, insertable = false)
	private Register register;

	@ManyToOne
	@JoinColumn(name = "category_id", updatable = false, insertable = false)
	private Category category;

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

	@Column(name = "status")
	private Integer status;

	@Column(name = "payment_date")
	private LocalDate paymentDate;

	@Column(name = "due_date")
	private LocalDate dueDate;

	@Column(name = "category_id")
	private Long idCategory;

	@Column(name = "receipt_payment")
	private byte[] receiptPayment;


	public Debt() {
	}

	protected Debt(Long idDebt, LocalDate inputDate, String debtDescription,
				   Double value, Long idRegister, Integer status, LocalDate paymentDate,
				   byte[] receiptPayment, LocalDate dueDate, Long idCategory) {
		this.idDebt = idDebt;
		this.inputDate = inputDate;
		this.debtDescription = debtDescription;
		this.value = value;
		this.idRegister = idRegister;
		this.status = status;
		this.paymentDate = paymentDate;
		this.receiptPayment = receiptPayment;
		this.dueDate = dueDate;
		this.idCategory = idCategory;
	}

	public static final class Builder {
		private Long idDebt;
		private LocalDate inputDate;
		private String debtDescription;
		private Double value;
		private Long idRegister;
		private Integer status;
		private LocalDate paymentDate;
		private byte[] receiptPayment;
		private LocalDate dueDate;
		private Long idCategory;

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

		public Builder receiptPayment(byte[] val) {
			receiptPayment = val;
			return this;
		}

		public Builder paymentDate(LocalDate val) {
			paymentDate = val;
			return this;
		}

		public Builder dueDate(LocalDate val) {
			dueDate = val;
			return this;
		}

		public Builder idCategory(Long val) {
			idCategory = val;
			return this;
		}

		public Builder status(Integer val) {
			status = val;
			return this;
		}

		public Debt createDebt() {
			return new Debt(
					idDebt, inputDate, debtDescription, value, idRegister, status, paymentDate, receiptPayment, dueDate, idCategory

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

	public void setPaymentDate(LocalDate paymentDate) {
		this.paymentDate = paymentDate;
	}

	public byte[] getReceiptPayment() {
		return receiptPayment;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	public LocalDate getPaymentDate() {
		return paymentDate;
	}

	public void setReceiptPayment(byte[] receiptPayment) {
		this.receiptPayment = receiptPayment;
	}

	public Long getIdCategory() {
		return idCategory;
	}

	public void setIdCategory(Long idCategory) {
		this.idCategory = idCategory;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Debt debt = (Debt) o;
		return Objects.equals(idDebt, debt.idDebt) && Objects.equals(inputDate, debt.inputDate) && Objects.equals(debtDescription, debt.debtDescription) && Objects.equals(value, debt.value) && Objects.equals(idRegister, debt.idRegister) && Objects.equals(status, debt.status) && Objects.equals(paymentDate, debt.paymentDate) && Objects.equals(dueDate, debt.dueDate) && Objects.equals(idCategory, debt.idCategory);
	}

	@Override
	public int hashCode() {
		return Objects.hash(idDebt, inputDate, debtDescription, value, idRegister, status, paymentDate, dueDate, idCategory);
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", Debt.class.getSimpleName() + "[", "]")
				.add("idDebt=" + idDebt)
				.add("inputDate=" + inputDate)
				.add("debtDescription='" + debtDescription + "'")
				.add("value=" + value)
				.add("idRegister=" + idRegister)
				.add("status=" + status)
				.add("paymentDate=" + paymentDate)
				.add("dueDate=" + dueDate)
				.add("idCategory=" + idCategory)
				.toString();
	}
}
