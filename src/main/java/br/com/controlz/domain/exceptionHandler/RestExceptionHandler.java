package br.com.controlz.domain.exceptionHandler;

import br.com.controlz.domain.dto.ResponseEntityError;
import br.com.controlz.domain.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({
			PhoneException.class,
			EmailException.class,
			FieldException.class,
			ValueException.class
	})
	public ResponseEntityError handleBadRequest(Exception e) {
		return new ResponseEntityError(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, e.getMessage());
	}

	@ResponseStatus(HttpStatus.CONFLICT)
	@ExceptionHandler({
			RegisterException.class
	})
	public ResponseEntityError handleConflict(Exception e) {
		return new ResponseEntityError(HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT, e.getMessage());
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler({
			RegisterNotFoundException.class,
			DebtNotFoundException.class
	})
	public ResponseEntityError handleNotFound(Exception e) {
		return new ResponseEntityError(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND, e.getMessage());
	}
}
