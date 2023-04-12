package br.com.controlz.domain.dto;

public class FinancialHistoryDTO {

	private final Long financialHistoryId;
	private final Long registerId;
	private final Double totalCredit;
	private final Double totalDebt;
	private final String period;
	private final Double balanceCredit;

	protected FinancialHistoryDTO(Long financialHistoryId, Long registerId, Double totalCredit, Double totalDebt, String period, Double balanceCredit) {
		this.financialHistoryId = financialHistoryId;
		this.registerId = registerId;
		this.totalCredit = totalCredit;
		this.totalDebt = totalDebt;
		this.period = period;
		this.balanceCredit = balanceCredit;
	}

	public static final class Builder {
		private Long financialHistoryId;
		private Long registerId;
		private Double totalCredit;
		private Double totalDebt;
		private String period;
		private Double balanceCredit;

		public Builder() {
			//ignored
		}

		public Builder financialHistoryId(Long val) {
			financialHistoryId = val;
			return this;
		}

		public Builder registerId(Long val) {
			registerId = val;
			return this;
		}

		public Builder totalCredit(Double val) {
			totalCredit = val;
			return this;
		}

		public Builder totalDebt(Double val) {
			totalDebt = val;
			return this;
		}

		public Builder period(String val) {
			period = val;
			return this;
		}

		public Builder balanceCredit(Double val) {
			balanceCredit = val;
			return this;
		}

		public FinancialHistoryDTO createNewFinancialHistoryDTO() {
			return new FinancialHistoryDTO(
					financialHistoryId, registerId, totalCredit,
					totalDebt, period, balanceCredit
			);
		}
	}

	public Long getFinancialHistoryId() {
		return financialHistoryId;
	}

	public Long getRegisterId() {
		return registerId;
	}

	public Double getTotalCredit() {
		return totalCredit;
	}

	public Double getTotalDebt() {
		return totalDebt;
	}

	public String getPeriod() {
		return period;
	}

	public Double getBalanceCredit() {
		return balanceCredit;
	}
}
