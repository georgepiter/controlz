package br.com.controlz.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DebtValueDashDTO {

	private Double totalDebt;
	private Double totalEntryValue;
	private Double currentTotalValue;

	public DebtValueDashDTO(Double totalDebt, Double totalEntryValue, Double currentTotalValue) {
		this.totalDebt = totalDebt;
		this.totalEntryValue = totalEntryValue;
		this.currentTotalValue = currentTotalValue;
	}

	public DebtValueDashDTO() {

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

	public void setTotalDebt(Double totalDebt) {
		this.totalDebt = totalDebt;
	}

	public void setTotalEntryValue(Double totalEntryValue) {
		this.totalEntryValue = totalEntryValue;
	}

	public void setCurrentTotalValue(Double currentTotalValue) {
		this.currentTotalValue = currentTotalValue;
	}
}
