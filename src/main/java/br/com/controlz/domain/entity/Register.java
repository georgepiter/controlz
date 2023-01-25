package br.com.controlz.domain.entity;

import br.com.controlz.domain.entity.security.User;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "register")
public class Register implements Serializable {

	@OneToMany(mappedBy = "register", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	private final List<Debt> debts = new ArrayList<>();

	@OneToMany(mappedBy = "register", cascade = CascadeType.REMOVE)
	private final List<FinancialHistory> financialHistories = new ArrayList<>();

	@ManyToOne
	@JoinColumn(name = "user_id", updatable = false, insertable = false)
	private User user;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long registerId;

	@Column(name = "registration_date")
	private LocalDate registrationDate;

	@Column(name = "cell")
	private String cell;

	@Column(name = "others")
	private Double others;

	@Column(name = "salary")
	private Double salary;

	@Column(name = "photo")
	private byte[] photo;

	@Column(name = "update_date")
	private LocalDate updateDate;

	@Column(name = "user_id")
	private Long userId;

	public Register() {
	}

	protected Register(Long registerId, LocalDate registrationDate,
					   String cell, Double others, Double salary,
					   byte[] photo, LocalDate updateDate, Long userId) {
		this.registerId = registerId;
		this.registrationDate = registrationDate;
		this.cell = cell;
		this.others = others;
		this.salary = salary;
		this.photo = photo;
		this.updateDate = updateDate;
		this.userId = userId;
	}

	public static final class Builder {
		private Long registerId;
		private LocalDate registrationDate;
		private String cell;
		private Double others;
		private Double salary;
		private byte[] photo;
		private LocalDate update;
		private Long userId;

		public Builder() {
			//ignored
		}

		public Builder registerId(Long val) {
			registerId = val;
			return this;
		}

		public Builder registrationDate(LocalDate val) {
			registrationDate = val;
			return this;
		}

		public Builder cell(String val) {
			cell = val;
			return this;
		}

		public Builder others(Double val) {
			others = val;
			return this;
		}

		public Builder salary(Double val) {
			salary = val;
			return this;
		}

		public Builder photo(byte[] val) {
			photo = val;
			return this;
		}

		public Builder update(LocalDate val) {
			update = val;
			return this;
		}

		public Builder userId(Long val) {
			userId = val;
			return this;
		}

		public Register createNewRegister() {
			return new Register(
					registerId, registrationDate,
					cell, others, salary, photo, update, userId
			);
		}
	}

	public Long getRegisterId() {
		return registerId;
	}

	public LocalDate getRegistrationDate() {
		return registrationDate;
	}

	public String getCell() {
		return cell;
	}

	public Double getOthers() {
		return others;
	}

	public Double getSalary() {
		return salary;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public Long getUserId() {
		return userId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Register register = (Register) o;
		return Objects.equals(registerId, register.registerId) && Objects.equals(registrationDate, register.registrationDate) && Objects.equals(cell, register.cell) && Objects.equals(others, register.others) && Objects.equals(salary, register.salary) && Objects.equals(updateDate, register.updateDate) && Objects.equals(userId, register.userId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(registerId, registrationDate, cell, others, salary, updateDate, userId);
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", Register.class.getSimpleName() + "[", "]")
				.add("registerId=" + registerId)
				.add("registrationDate=" + registrationDate)
				.add("cell='" + cell + "'")
				.add("others=" + others)
				.add("salary=" + salary)
				.add("updateDate=" + updateDate)
				.add("userId=" + userId)
				.toString();
	}
}
