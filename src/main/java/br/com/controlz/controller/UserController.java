package br.com.controlz.controller;

import br.com.controlz.domain.dto.ResponseEntityError;
import br.com.controlz.domain.dto.UserDto;
import br.com.controlz.domain.exception.EmailException;
import br.com.controlz.domain.exception.UserException;
import br.com.controlz.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/user")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping(value = "/create")
	public ResponseEntity<HttpStatus> registerNewUser(@RequestBody UserDto user) throws EmailException, UserException {
		return userService.registerNewUser(user);
	}

//	@GetMapping(value = "/name/{name}") //todo parei aqui configurar o resto dos services e depois configurar o spring securtiry para testar o erro do bcrypt
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
