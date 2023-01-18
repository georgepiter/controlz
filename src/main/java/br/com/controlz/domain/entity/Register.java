package br.com.controlz.domain.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "register")
public class Register implements Serializable {

	@OneToMany(mappedBy = "register", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	private final List<Debt> debts = new ArrayList<>();

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long idRegister;

	@Column(name = "name")
	private String name;

	@Column(name = "registration_date")
	private LocalDate registrationDate;

	@Column(name = "email")
	private String email;

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

	public Register() {
	}

	protected Register(Long idRegister, String name, LocalDate registrationDate, String email, String cell, Double others, Double salary, byte[] photo, LocalDate updateDate) {
		this.idRegister = idRegister;
		this.name = name;
		this.registrationDate = registrationDate;
		this.email = email;
		this.cell = cell;
		this.others = others;
		this.salary = salary;
		this.photo = photo;
		this.updateDate = updateDate;
	}

	public static final class Builder {
		private Long id;
		private String name;
		private LocalDate registrationDate;
		private String email;
		private String cell;
		private Double others;
		private Double salary;
		private byte[] photo;
		private LocalDate update;

		public Builder() {
			//ignored
		}

		public Builder id(Long val) {
			id = val;
			return this;
		}

		public Builder name(String val) {
			name = val;
			return this;
		}

		public Builder registrationDate(LocalDate val) {
			registrationDate = val;
			return this;
		}

		public Builder email(String val) {
			email = val;
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

		public Register createNewRegister() {
			return new Register(
					id, name, registrationDate, email,
					cell, others, salary, photo, update
			);
		}
	}


	public Long getIdRegister() {
		return idRegister;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(LocalDate registrationDate) {
		this.registrationDate = registrationDate;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCell() {
		return cell;
	}

	public void setCell(String cell) {
		this.cell = cell;
	}

	public Double getOthers() {
		return others;
	}

	public void setOthers(Double others) {
		this.others = others;
	}

	public Double getSalary() {
		return salary;
	}

	public void setSalary(Double salary) {
		this.salary = salary;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public LocalDate getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(LocalDate updateDate) {
		this.updateDate = updateDate;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Register register = (Register) o;
		return Objects.equals(idRegister, register.idRegister) && Objects.equals(name, register.name) && Objects.equals(registrationDate, register.registrationDate) && Objects.equals(email, register.email) && Objects.equals(cell, register.cell) && Objects.equals(others, register.others) && Objects.equals(salary, register.salary) && Objects.equals(updateDate, register.updateDate);
	}

	@Override
	public int hashCode() {
		return Objects.hash(idRegister, name, registrationDate, email, cell, others, salary, updateDate);
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", Register.class.getSimpleName() + "[", "]")
				.add("idRegister=" + idRegister)
				.add("name='" + name + "'")
				.add("registrationDate=" + registrationDate)
				.add("email='" + email + "'")
				.add("cell='" + cell + "'")
				.add("others=" + others)
				.add("salary=" + salary)
				.add("photo=" + Arrays.toString(photo))
				.add("updateDate=" + updateDate)
				.toString();
	}
}
