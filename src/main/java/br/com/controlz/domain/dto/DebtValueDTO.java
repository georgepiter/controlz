package br.com.controlz.domain.dto;

import java.util.List;

public class DebtValueDTO {

	private List<DebtDTO> debtList;
	private Double totalDebt;
	private Double totalEntryValue;
	private Double currentTotalValue;

	protected DebtValueDTO(List<DebtDTO> debtList, Double totalDebt, Double totalEntryValue, Double currentTotalValue) {
		this.debtList = debtList;
		this.totalDebt = totalDebt;
		this.totalEntryValue = totalEntryValue;
		this.currentTotalValue = currentTotalValue;
	}

	public static final class Builder {
		private List<DebtDTO> debtList;
		private Double totalDebt;
		private Double totalEntryValue;
		private Double currentTotalValue;

		public Builder() {
			//ignored
		}

		public Builder debtList(List<DebtDTO> val) {
			debtList = val;
			return this;
		}

		public Builder totalDebt(double val) {
			totalDebt = val;
			return this;
		}

		public Builder totalEntryValue(Double val) {
			totalEntryValue = val;
			return this;
		}

		public Builder currentTotalValue(Double val) {
			currentTotalValue = val;
			return this;
		}

		public DebtValueDTO createNewDebtValue() {
			return new DebtValueDTO(
					debtList, totalDebt, totalEntryValue, currentTotalValue
			);
		}
	}

	public List<DebtDTO> getDebtList() {
		return debtList;
	}

	public Double getTotalDebt() {
		return totalDebt;
	}

	public Double getTotalEntryValue() {
		return totalEntryValue;
	}

	public Double getCurrentTotalValue() {
		return currentTotalValue;
	}

}
