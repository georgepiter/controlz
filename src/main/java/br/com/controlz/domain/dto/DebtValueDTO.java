package br.com.controlz.domain.dto;

import java.util.List;

public class DebtValueDTO {

	private String name;
	private List<DebtDTO> debtList;
	private Double totalDebt;
	private Double totalEntryValue;
	private Double currentTotalValue;

	protected DebtValueDTO(String name, List<DebtDTO> debtList, Double totalDebt, Double totalEntryValue, Double currentTotalValue) {
		this.name = name;
		this.debtList = debtList;
		this.totalDebt = totalDebt;
		this.totalEntryValue = totalEntryValue;
		this.currentTotalValue = currentTotalValue;
	}

	public static final class Builder {
		private String name;
		private List<DebtDTO> debtList;
		private Double totalDebt;
		private Double totalEntryValue;
		private Double currentTotalValue;

		public Builder() {
			//ignored
		}

		public Builder name(String val) {
			name = val;
			return this;
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
					name, debtList, totalDebt, totalEntryValue, currentTotalValue
			);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<DebtDTO> getDebtList() {
		return debtList;
	}

	public void setDebtList(List<DebtDTO> debtList) {
		this.debtList = debtList;
	}

	public Double getTotalDebt() {
		return totalDebt;
	}

	public void setTotalDebt(Double totalDebt) {
		this.totalDebt = totalDebt;
	}

	public Double getTotalEntryValue() {
		return totalEntryValue;
	}

	public void setTotalEntryValue(Double totalEntryValue) {
		this.totalEntryValue = totalEntryValue;
	}

	public Double getCurrentTotalValue() {
		return currentTotalValue;
	}

	public void setCurrentTotalValue(Double currentTotalValue) {
		this.currentTotalValue = currentTotalValue;
	}
}
