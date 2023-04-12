package br.com.controlz.controller;

import br.com.controlz.domain.dto.auth.AuthenticationRequestDTO;
import br.com.controlz.domain.dto.auth.AuthenticationResponseDTO;
import br.com.controlz.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "Token", produces = MediaType.APPLICATION_JSON_VALUE, tags = {"Token"})
@RequestMapping("api/v1/")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("login")
	@ApiOperation(value = "Realiza login e gera o token", response = AuthenticationResponseDTO.class)
	public ResponseEntity<?> login(@RequestBody AuthenticationRequestDTO request) {
		return authService.getToken(request);
	}
}