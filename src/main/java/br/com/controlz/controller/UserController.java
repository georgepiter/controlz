package br.com.controlz.controller;

import br.com.controlz.domain.dto.UserDTO;
import br.com.controlz.domain.exception.EmailException;
import br.com.controlz.domain.exception.UserException;
import br.com.controlz.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "Utilizador", produces = MediaType.APPLICATION_JSON_VALUE, tags = {"Utilizador do sistema"})
@RequestMapping("api/v1/user")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping(value = "/create")
	@ApiOperation(value = "Método que registra um novo utilizador")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	public ResponseEntity<HttpStatus> registerNewUser(@RequestBody UserDTO user) throws EmailException, UserException {
			return userService.registerNewUser(user);
	}

//	@GetMapping(value = "/name/{name}") //todo voltar aqui e depois de implementar os services como troca de senha  e gerar nova senha implementar o envio da senha pelo email sendblue
//	public ResponseEntity<HttpStatus> getUserByName(@PathVariable String name) {
//		return userService.getUserByName(name);
//	}
//
//	@PutMapping(value = "/")
//	public ResponseEntityError updateUser(@RequestBody UserDto user) {
//		return userService.updateUserNameAndRole(user);
//	}
//
//	@DeleteMapping(value = "/name/{name}")
//	public ResponseEntityError deleteUserByName(@PathVariable String name) throws UsernameNotFoundException {
//		return userService.deleteUserByName(name);
//	}
}
