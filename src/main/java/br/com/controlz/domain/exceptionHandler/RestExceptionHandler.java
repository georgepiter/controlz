package br.com.controlz.domain.exceptionHandler;

import br.com.controlz.domain.dto.ResponseEntityCustom;
import br.com.controlz.domain.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
public class RestExceptionHandler {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({
			PhoneException.class,
			EmailException.class,
			FieldException.class,
			ValueException.class,
			EmailException.class,
			EmailSenderException.class
	})
	public ResponseEntityCustom handleBadRequest(Exception e) {
		return new ResponseEntityCustom(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, e.getMessage());
	}

	@ResponseStatus(HttpStatus.CONFLICT)
	@ExceptionHandler({
			RegisterException.class,
			UserException.class
	})
	public ResponseEntityCustom handleConflict(Exception e) {
		return new ResponseEntityCustom(HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT, e.getMessage());
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler({
			RegisterNotFoundException.class,
			DebtNotFoundException.class,
			UsernameNotFoundException.class,
			EmailNotFoundException.class,
			CategoryNotFoundException.class
	})
	public ResponseEntityCustom handleNotFound(Exception e) {
		return new ResponseEntityCustom(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND, e.getMessage());
	}

	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler({
			AuthenticationException.class,
			AuthInvalidException.class
	})
	public ResponseEntityCustom handleForbidden(Exception e) {
		return new ResponseEntityCustom(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN, e.getMessage());
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler({
			SQLIntegrityConstraintViolationException.class
	})
	public ResponseEntityCustom handleInternalServerError(Exception e) {
		return new ResponseEntityCustom(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
	}
}
