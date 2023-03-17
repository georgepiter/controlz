package br.com.controlz.domain.dto;

public class CategoryDTO {

	private Long categoryId;
	private String description;

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
}
