package br.com.controlz.controller;

import br.com.controlz.domain.dto.RegisterDTO;
import br.com.controlz.domain.exception.*;
import br.com.controlz.service.RegisterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/v1/register")
public class RegisterController {

	private final RegisterService registerService;

	public RegisterController(RegisterService registerService) {
		this.registerService = registerService;
	}

	@PostMapping(value = "/")
	public ResponseEntity<HttpStatus> registerNewPerson(@RequestBody RegisterDTO registerDTO) throws ValueException, RegisterException, PhoneException, EmailException, FieldException {
		return registerService.registerNewPerson(registerDTO);
	}
}
