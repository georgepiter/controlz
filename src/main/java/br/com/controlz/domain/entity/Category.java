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

	@OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
	private final List<Debt> debts = new ArrayList<>();

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long categoryId;

	@Column(name = "description")
	private String description;

	public Category(String description) {
		this.description = description;
	}

	public Category() {
	}

	public List<Debt> getDebts() {
		return debts;
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Category category = (Category) o;
		return Objects.equals(categoryId, category.categoryId) && Objects.equals(description, category.description);
	}

	@Override
	public int hashCode() {
		return Objects.hash(categoryId, description);
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", Category.class.getSimpleName() + "[", "]")
				.add("categoryId=" + categoryId)
				.add("description='" + description + "'")
				.toString();
	}
}
