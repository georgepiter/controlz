package br.com.controlz.domain.dto;

import org.springframework.http.HttpStatus;

public class ResponseEntityError {

	private Integer status;
	private HttpStatus error;
	private String message;

	public ResponseEntityError(Integer status, HttpStatus error, String message) {
		this.status = status;
		this.error = error;
		this.message = message;
	}

	public ResponseEntityError() {
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public HttpStatus getError() {
		return error;
	}

	public void setError(HttpStatus error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
