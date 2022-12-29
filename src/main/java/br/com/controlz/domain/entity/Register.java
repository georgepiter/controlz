package br.com.controlz.domain.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "register")
public class Register implements Serializable {

	@OneToMany(mappedBy = "id", cascade = CascadeType.REMOVE)
	private final List<Debt> debts = new ArrayList<>();

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

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

	public Register() {
	}

	protected Register(String name, LocalDate registrationDate, String email, String cell, Double others, Double salary, byte[] photo) {
		this.name = name;
		this.registrationDate = registrationDate;
		this.email = email;
		this.cell = cell;
		this.others = others;
		this.salary = salary;
		this.photo = photo;
	}

	public static final class Builder {

		private String name;
		private LocalDate registrationDate;
		private String email;
		private String cell;
		private Double others;
		private Double salary;
		private byte[] photo;

		public Builder() {
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

		public Register createNewRegister() {
			return new Register(
					name, registrationDate, email,
					cell, others, salary, photo
			);
		}
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Register register = (Register) o;
		return Objects.equals(id, register.id) && Objects.equals(name, register.name) && Objects.equals(registrationDate, register.registrationDate) && Objects.equals(email, register.email) && Objects.equals(cell, register.cell) && Objects.equals(others, register.others) && Objects.equals(salary, register.salary) && Arrays.equals(photo, register.photo);
	}

	@Override
	public int hashCode() {
		int result = Objects.hash(id, name, registrationDate, email, cell, others, salary);
		result = 31 * result + Arrays.hashCode(photo);
		return result;
	}

	@Override
	public String toString() {
		return "Register{" + "name='" + name + '\'' +
				", registrationDate=" + registrationDate +
				", email='" + email + '\'' +
				", cell='" + cell + '\'' +
				", others=" + others +
				", salary=" + salary +
				", photo=" + Arrays.toString(photo) +
				'}';
	}
}
