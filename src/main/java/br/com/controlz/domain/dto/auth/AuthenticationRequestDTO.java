package br.com.controlz.domain.dto.auth;

public class AuthenticationRequestDTO {

	private final String userName;
	private final String password;

	public AuthenticationRequestDTO(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}
}
