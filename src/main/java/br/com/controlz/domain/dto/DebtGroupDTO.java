package br.com.controlz.domain.dto;

import java.util.List;

public class DebtGroupDTO extends DebtValueDashDTO {

	private List<DebtCategoryGroupDTO> debtsCategoryGroupDTO;

	public DebtGroupDTO() {
	}

	public DebtGroupDTO(Double totalDebt, Double totalEntryValue, Double currentTotalValue) {
		super(totalDebt, totalEntryValue, currentTotalValue);
	}

	public List<DebtCategoryGroupDTO> getDebtsCategoryGroupDTO() {
		return debtsCategoryGroupDTO;
	}

	public void setDebtsCategoryGroupDTO(List<DebtCategoryGroupDTO> debtsCategoryGroupDTO) {
		this.debtsCategoryGroupDTO = debtsCategoryGroupDTO;
	}

	@Override
	public String toString() {
		return "DebtGroupDTO{" +
				"debtsCategoryGroupDTO=" + debtsCategoryGroupDTO +
				'}';
	}
}
