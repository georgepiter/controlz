package br.com.controlz.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DebtDTO {

	private final Long debtId;
	private Long userId;
	private String status;
	private final byte[] receiptPayment;
	private final LocalDate dueDate;
	private LocalDate paymentDate;
	private final Long categoryId;
	private final Long registerId;
	private final LocalDate inputDate;
	private final String debtDescription;
	private Double value;


	protected DebtDTO(Long debtId, Long registerId, LocalDate inputDate,
					  String debtDescription, Double value, String status,
					  LocalDate paymentDate, byte[] receiptPayment, LocalDate dueDate, Long categoryId, Long userId) {
		this.debtId = debtId;
		this.paymentDate = paymentDate;
		this.receiptPayment = receiptPayment;
		this.dueDate = dueDate;
		this.categoryId = categoryId;
		this.registerId = registerId;
		this.inputDate = inputDate;
		this.debtDescription = debtDescription;
		this.value = value;
		this.status = status;
		this.userId = userId;
	}

	public static final class Builder {
		private String status;
		private LocalDate paymentDate;
		private byte[] receiptPayment;
		private LocalDate dueDate;
		private Long categoryId;
		private Long debtId;
		private Long registerId;
		private LocalDate inputDate;
		private String debtDescription;
		private Double value;
		private Long userId;

		public Builder() {
			//ignored
		}

		public Builder debtId(Long val) {
			debtId = val;
			return this;
		}

		public Builder registerId(Long val) {
			registerId = val;
			return this;
		}

		public Builder dueDate(LocalDate val) {
			dueDate = val;
			return this;
		}

		public Builder inputDate(LocalDate val) {
			inputDate = val;
			return this;
		}

		public Builder paymentDate(LocalDate val) {
			paymentDate = val;
			return this;
		}

		public Builder debtDescription(String val) {
			debtDescription = val;
			return this;
		}

		public Builder categoryId(Long val) {
			categoryId = val;
			return this;
		}

		public Builder receiptPayment(byte[] val) {
			receiptPayment = val;
			return this;
		}

		public Builder status(String val) {
			status = val;
			return this;
		}

		public Builder value(Double val) {
			value = val;
			return this;
		}

		public Builder userId(Long val) {
			userId = val;
			return this;
		}

		public DebtDTO createNewDebtDTO() {
			return new DebtDTO(
					debtId, registerId, inputDate, debtDescription, value, status, paymentDate, receiptPayment, dueDate, categoryId, userId
			);
		}
	}

	public Long getDebtId() {
		return debtId;
	}

	public Long getRegisterId() {
		return registerId;
	}

	public String getDebtDescription() {
		return debtDescription;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public byte[] getReceiptPayment() {
		return receiptPayment;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public LocalDate getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(LocalDate paymentDate) {
		this.paymentDate = paymentDate;
	}
}
