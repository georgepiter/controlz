package br.com.controlz.domain.exception;

public class JWTExpiresException extends Exception {
	public JWTExpiresException(String message) {
		super(message);
	}
}
