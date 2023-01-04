package br.com.controlz.domain.dto;

import java.util.List;

public class DebtValueDTO {

	private String name;
	private List<DebtDTO> debtList;
	private Double totalDebt;
	private Double entryDebtValue;
	private Double currentDebtValue;

	protected DebtValueDTO(String name, List<DebtDTO> debtList, Double totalDebt, Double entryDebtValue, Double currentDebtValue) {
		this.name = name;
		this.debtList = debtList;
		this.totalDebt = totalDebt;
		this.entryDebtValue = entryDebtValue;
		this.currentDebtValue = currentDebtValue;
	}

	public static final class Builder {
		private String name;
		private List<DebtDTO> debtList;
		private Double totalDebt;
		private Double entryDebtValue;
		private Double currentDebtValue;

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

		public Builder entryDebtValue(Double val) {
			entryDebtValue = val;
			return this;
		}

		public Builder currentDebtValue(Double val) {
			currentDebtValue = val;
			return this;
		}

		public DebtValueDTO createNewDebtValue() {
			return new DebtValueDTO(
					name, debtList, totalDebt, entryDebtValue, currentDebtValue
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

	public Double getEntryDebtValue() {
		return entryDebtValue;
	}

	public void setEntryDebtValue(Double entryDebtValue) {
		this.entryDebtValue = entryDebtValue;
	}

	public Double getCurrentDebtValue() {
		return currentDebtValue;
	}

	public void setCurrentDebtValue(Double currentDebtValue) {
		this.currentDebtValue = currentDebtValue;
	}
}
