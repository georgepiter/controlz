package br.com.controlz.domain.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

@Entity
@Table(name = "category")
public class Category implements Serializable {

	@OneToMany(mappedBy = "category")
	private final List<Debt> debts = new ArrayList<>();

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long idCategory;

	@Column(name = "description")
	private String description;

	public Category(Long idCategory, String description) {
		this.idCategory = idCategory;
		this.description = description;
	}

	public List<Debt> getDebts() {
		return debts;
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Category category = (Category) o;
		return Objects.equals(debts, category.debts) && Objects.equals(idCategory, category.idCategory) && Objects.equals(description, category.description);
	}

	@Override
	public int hashCode() {
		return Objects.hash(debts, idCategory, description);
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", Category.class.getSimpleName() + "[", "]")
				.add("debts=" + debts)
				.add("idCategory=" + idCategory)
				.add("description='" + description + "'")
				.toString();
	}
}
