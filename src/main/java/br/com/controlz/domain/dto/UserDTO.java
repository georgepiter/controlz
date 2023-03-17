package br.com.controlz.domain.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class UserDTO {

	@NotNull
	private String name;

	@NotNull
	private String password;

	@Email
	private String email;

	@NotNull
	private Long roleId;
	private Long userId;
	private String status;
	private String perfil;

	protected UserDTO(Long userId, String name, String password, String email, Long roleId, String status, String perfil) {
		this.userId = userId;
		this.name = name;
		this.password = password;
		this.email = email;
		this.roleId = roleId;
		this.status = status;
		this.perfil = perfil;
	}

	public UserDTO() {
	}

	public static final class Builder {
		private Long userId;
		private String name;
		private String password;
		private String email;
		private Long roleId;
		private String status;
		private String perfil;

		public Builder() {
			//ignored
		}

		public Builder userId(Long val) {
			userId = val;
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

		public Builder roleId(Long val) {
			roleId = val;
			return this;
		}

		public Builder status(String val) {
			status = val;
			return this;
		}

		public Builder perfil(String val) {
			perfil = val;
			return this;
		}

		public UserDTO createNewUser() {
			return new UserDTO(
					userId, name, password, email,
					roleId, status, perfil
			);
		}
	}

	public Long getUserId() {
		return userId;
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}

	public String getEmail() {
		return email;
	}

	public Long getRoleId() {
		return roleId;
	}

	public String getStatus() {
		return status;
	}

	public String getPerfil() {
		return perfil;
	}

}
