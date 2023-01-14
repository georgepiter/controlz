package br.com.controlz.domain.dto;

public class CategoryDTO {

	private Long idCategory;
	private String description;

	public CategoryDTO() {
	}

	public Long getIdCategory() {
		return idCategory;
	}

	public void setIdCategory(Long idCategory) {
		this.idCategory = idCategory;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
