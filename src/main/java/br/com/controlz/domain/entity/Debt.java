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
	private Long debtId;

	@Column(name = "input_date")
	private LocalDate inputDate;

	@Column(name = "debt_description")
	private String debtDescription;

	@Column(name = "value")
	private Double value;

	@Column(name = "register_id")
	private Long registerId;

	@Column(name = "status")
	private Integer status;

	@Column(name = "payment_date")
	private LocalDate paymentDate;

	@Column(name = "due_date")
	private LocalDate dueDate;

	@Column(name = "category_id")
	private Long categoryId;

	@Column(name = "receipt_payment")
	private byte[] receiptPayment;

	public Debt() {
	}

	protected Debt(Long debtId, LocalDate inputDate, String debtDescription,
				   Double value, Long registerId, Integer status, LocalDate paymentDate,
				   byte[] receiptPayment, LocalDate dueDate, Long categoryId) {
		this.debtId = debtId;
		this.inputDate = inputDate;
		this.debtDescription = debtDescription;
		this.value = value;
		this.registerId = registerId;
		this.status = status;
		this.paymentDate = paymentDate;
		this.receiptPayment = receiptPayment;
		this.dueDate = dueDate;
		this.categoryId = categoryId;
	}

	public static final class Builder {
		private Long debtId;
		private LocalDate inputDate;
		private String debtDescription;
		private Double value;
		private Long registerId;
		private Integer status;
		private LocalDate paymentDate;
		private byte[] receiptPayment;
		private LocalDate dueDate;
		private Long categoryId;

		public Builder() {
			//ignored
		}

		public Builder debtId(Long val) {
			debtId = val;
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

		public Builder registerId(Long val) {
			registerId = val;
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

		public Builder categoryId(Long val) {
			categoryId = val;
			return this;
		}

		public Builder status(Integer val) {
			status = val;
			return this;
		}

		public Debt createDebt() {
			return new Debt(
					debtId, inputDate, debtDescription, value, registerId, status, paymentDate, receiptPayment, dueDate, categoryId

			);
		}
	}

	public Long getDebtId() {
		return debtId;
	}

	public Long getRegisterId() {
		return registerId;
	}

	public LocalDate getInputDate() {
		return inputDate;
	}

	public String getDebtDescription() {
		return debtDescription;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public void setPaymentDate(LocalDate paymentDate) {
		this.paymentDate = paymentDate;
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

	public LocalDate getPaymentDate() {
		return paymentDate;
	}

	public byte[] getReceiptPayment() {
		return receiptPayment;
	}

	public void setReceiptPayment(byte[] receiptPayment) {
		this.receiptPayment = receiptPayment;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Debt debt = (Debt) o;
		return Objects.equals(debtId, debt.debtId) && Objects.equals(inputDate, debt.inputDate) && Objects.equals(debtDescription, debt.debtDescription) && Objects.equals(value, debt.value) && Objects.equals(registerId, debt.registerId) && Objects.equals(status, debt.status) && Objects.equals(paymentDate, debt.paymentDate) && Objects.equals(dueDate, debt.dueDate) && Objects.equals(categoryId, debt.categoryId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(debtId, inputDate, debtDescription, value, registerId, status, paymentDate, dueDate, categoryId);
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", Debt.class.getSimpleName() + "[", "]")
				.add("idDebt=" + debtId)
				.add("inputDate=" + inputDate)
				.add("debtDescription='" + debtDescription + "'")
				.add("value=" + value)
				.add("idRegister=" + registerId)
				.add("status=" + status)
				.add("paymentDate=" + paymentDate)
				.add("dueDate=" + dueDate)
				.add("categoryId=" + categoryId)
				.toString();
	}
}
