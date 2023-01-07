package br.com.controlz.domain.dto;

import br.com.controlz.domain.entity.security.User;
import br.com.controlz.domain.enums.RoleEnum;
import br.com.controlz.domain.enums.StatusEnum;

public class UserDTO {

	private Long idUser;
	private String name;
	private String password;
	private String email;
	private Long idRole;
	private String status;
	private String perfil;

	public UserDTO() {
	}

	protected UserDTO(Long idUser, String name, String password, String email, Long idRole, String status, String perfil) {
		this.idUser = idUser;
		this.name = name;
		this.password = password;
		this.email = email;
		this.idRole = idRole;
		this.status = status;
		this.perfil = perfil;
	}

	public UserDTO(User user) {
		name = user.getName();
		email = user.getEmail();
		idRole = user.getIdRole();
		status = user.getStatus().equals(StatusEnum.ACTIVE.getValue()) ? "ACTIVE" : "INACTIVE";
		perfil = user.getIdRole().equals(RoleEnum.ADMIN.getCod()) ? "ADMIN" : "MANAGER";
	}

	public static final class Builder {
		private Long idUser;
		private String name;
		private String password;
		private String email;
		private Long idRole;
		private String status;
		private String perfil;

		public Builder() {
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

		public Builder idRole(Long val) {
			idRole = val;
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
					idUser, name, password, email,
					idRole, status, perfil
			);
		}
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

	public Long getIdRole() {
		return idRole;
	}

	public void setIdRole(Long idRole) {
		this.idRole = idRole;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPerfil() {
		return perfil;
	}

	public void setPerfil(String perfil) {
		this.perfil = perfil;
	}
}
