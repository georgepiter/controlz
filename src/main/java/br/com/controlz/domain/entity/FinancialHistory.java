package br.com.controlz.domain.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.StringJoiner;

@Entity
@Table(name = "financial_history")
public class FinancialHistory implements Serializable {

	@ManyToOne
	@JoinColumn(name = "register_id", updatable = false, insertable = false)
	private Register register;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long financialHistoryId;

	@Column(name = "total_credit")
	private Double totalCredit;

	@Column(name = "total_debt")
	private Double totalDebt;

	@Column(name = "register_id")
	private Long registerId;

	@Column(name = "period")
	private String period;

	@Column(name = "balance_credit")
	private Double balanceCredit;

	public FinancialHistory() {
	}

	protected FinancialHistory(Long financialHistoryId, Double totalCredit, Double totalDebt, Long registerId, String period, Double balanceCredit) {
		this.financialHistoryId = financialHistoryId;
		this.totalCredit = totalCredit;
		this.totalDebt = totalDebt;
		this.registerId = registerId;
		this.period = period;
		this.balanceCredit = balanceCredit;
	}

	public static final class Builder {
		private Long financialHistoryId;
		private Double totalCredit;
		private Double totalDebt;
		private Long registerId;
		private String period;
		private Double balanceCredit;

		public Builder() {
			//ignored
		}

		public Builder financialHistoryId(Long val) {
			financialHistoryId = val;
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

		public Builder registerId(Long val) {
			registerId = val;
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

		public FinancialHistory createNewFinancialHistory() {
			return new FinancialHistory(
					financialHistoryId, totalCredit,
					totalDebt, registerId, period, balanceCredit
			);
		}
	}

	public Long getFinancialHistoryId() {
		return financialHistoryId;
	}

	public Double getTotalCredit() {
		return totalCredit;
	}

	public Double getTotalDebt() {
		return totalDebt;
	}

	public Long getRegisterId() {
		return registerId;
	}

	public String getPeriod() {
		return period;
	}

	public Double getBalanceCredit() {
		return balanceCredit;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		FinancialHistory that = (FinancialHistory) o;
		return Objects.equals(financialHistoryId, that.financialHistoryId) && Objects.equals(totalCredit, that.totalCredit) && Objects.equals(totalDebt, that.totalDebt) && Objects.equals(registerId, that.registerId) && Objects.equals(period, that.period) && Objects.equals(balanceCredit, that.balanceCredit);
	}

	@Override
	public int hashCode() {
		return Objects.hash(financialHistoryId, totalCredit, totalDebt, registerId, period, balanceCredit);
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", FinancialHistory.class.getSimpleName() + "[", "]")
				.add("financialHistoryId=" + financialHistoryId)
				.add("totalCredit=" + totalCredit)
				.add("totalDebt=" + totalDebt)
				.add("registerId=" + registerId)
				.add("period='" + period + "'")
				.add("balanceCredit=" + balanceCredit)
				.toString();
	}
}
