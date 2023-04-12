package br.com.controlz.domain.dto;

import java.util.List;

public class DebtCategoryGroupDTO {
	private String typeCategory;
	private Long categoryId;
	private List<DebtDTO> debtList;

	public DebtCategoryGroupDTO() {
	}

	public String getTypeCategory() {
		return typeCategory;
	}

	public void setTypeCategory(String typeCategory) {
		this.typeCategory = typeCategory;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public List<DebtDTO> getDebtList() {
		return debtList;
	}

	public void setDebtList(List<DebtDTO> debtList) {
		this.debtList = debtList;
	}

	@Override
	public String toString() {
		return "DebtCategoryGroupDTO{" +
				"typeCategory='" + typeCategory + '\'' +
				", categoryId=" + categoryId +
				", debtList=" + debtList +
				'}';
	}
}
