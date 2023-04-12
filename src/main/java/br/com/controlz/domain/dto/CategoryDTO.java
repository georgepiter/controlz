package br.com.controlz.domain.dto;

public class CategoryDTO {

	private Long categoryId;
	private String description;
	private Long registerId;

	public CategoryDTO() {
		//ignored
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getRegisterId() {
		return registerId;
	}

	public void setRegisterId(Long registerId) {
		this.registerId = registerId;
	}
}
