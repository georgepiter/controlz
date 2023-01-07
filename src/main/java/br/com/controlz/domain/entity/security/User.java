package br.com.controlz.domain.entity.security;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

@Entity
@Table(name = "user")
public class User implements Serializable {

	@ManyToOne
	@JoinColumn(name = "role_id", updatable = false, insertable = false)
	private Role role;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long idUser;

	@Column(name = "username")
	private String name;

	@Column(name = "password")
	private String password;

	@Column(name = "email")
	private String email;

	@Column(name = "create_time")
	private LocalDateTime createTime;

	@Column(name = "role_id")
	private Long idRole;

	@Column(name = "status")
	private Integer status;

	public User() {
	}

	public User(Role role, Long idUser, String name, String password, String email, LocalDateTime createTime, Long idRole, Integer status) {
		this.role = role;
		this.idUser = idUser;
		this.name = name;
		this.password = password;
		this.email = email;
		this.createTime = createTime;
		this.idRole = idRole;
		this.status = status;
	}

	public static final class Builder {
		private Role role;
		private Long idUser;
		private String name;
		private String password;
		private String email;
		private LocalDateTime createTime;
		private Long idRole;
		private Integer status;

		public Builder() {
		}

		public Builder role(Role val) {
			role = val;
			return this;
		}

		public Builder idUser(Long val) {
			idUser = val;
			return this;
		}

		public Builder name(String val) {
			name = val;
			return this;
		}

		public Builder password(String val) {
			password = val;
			return this;
		}

		public Builder email(String val) {
			email = val;
			return this;
		}

		public Builder createTime(LocalDateTime val) {
			createTime = val;
			return this;
		}

		public Builder idRole(Long val) {
			idRole = val;
			return this;
		}

		public Builder status(Integer val) {
			status = val;
			return this;
		}

		public User createNewUser() {
			return new User(
					role, idUser, name, password, email, createTime, idRole, status);
		}
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Long getIdUser() {
		return idUser;
	}

	public void setIdUser(Long idUser) {
		this.idUser = idUser;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public Long getIdRole() {
		return idRole;
	}

	public void setIdRole(Long idRole) {
		this.idRole = idRole;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		User user = (User) o;
		return Objects.equals(role, user.role) && Objects.equals(idUser, user.idUser) && Objects.equals(name, user.name) && Objects.equals(password, user.password) && Objects.equals(email, user.email) && Objects.equals(createTime, user.createTime) && Objects.equals(idRole, user.idRole) && Objects.equals(status, user.status);
	}

	@Override
	public int hashCode() {
		return Objects.hash(role, idUser, name, password, email, createTime, idRole, status);
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", User.class.getSimpleName() + "[", "]")
				.add("role=" + role)
				.add("idUser=" + idUser)
				.add("name='" + name + "'")
				.add("password='" + password + "'")
				.add("email='" + email + "'")
				.add("createTime=" + createTime)
				.add("idRole=" + idRole)
				.add("status=" + status)
				.toString();
	}
}
