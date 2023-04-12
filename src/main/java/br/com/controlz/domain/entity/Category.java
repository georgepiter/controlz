package br.com.controlz.domain.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "category")
public class Category implements Serializable {

	@OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
	private final List<Debt> debts = new ArrayList<>();

	@ManyToOne
	@JoinColumn(name = "register_id", updatable = false, insertable = false)
	private Register register;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long categoryId;

	@Column(name = "description")
	private String description;

	@Column(name = "register_id")
	private Long registerId;

	public Category(String description, Long registerId) {
		this.description = description;
		this.registerId = registerId;
	}

	public Category() {
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Category category = (Category) o;
		return Objects.equals(categoryId, category.categoryId) && Objects.equals(description, category.description) && Objects.equals(registerId, category.registerId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(categoryId, description, registerId);
	}

	@Override
	public String toString() {
		return "Category{" +
				"categoryId=" + categoryId +
				", description='" + description + '\'' +
				", registerId=" + registerId +
				'}';
	}
}
