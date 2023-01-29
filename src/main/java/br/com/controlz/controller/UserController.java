package br.com.controlz.controller;

import br.com.controlz.domain.dto.ResponseEntityCustom;
import br.com.controlz.domain.dto.UserDTO;
import br.com.controlz.domain.exception.EmailException;
import br.com.controlz.domain.exception.EmailNotFoundException;
import br.com.controlz.domain.exception.UserException;
import br.com.controlz.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

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
	public ResponseEntityCustom registerNewUser(@RequestBody UserDTO user) throws EmailException, UserException {
		return userService.registerNewUser(user);
	}

	@PostMapping(value = "/reset")
	@ApiOperation(value = "Método que reseta a senha do utilizador")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
	public ResponseEntity<HttpStatus> resetPassword(@RequestBody UserDTO user) throws EmailNotFoundException, EmailException {
		return userService.resetPassword(user);
	}

	@PostMapping(value = "/forgot")
	@ApiOperation(value = "Método que reseta a senha do utilizador e envia por e-mail")
	public ResponseEntity<HttpStatus> resetPasswordAndSendToEmail(@RequestBody UserDTO user) throws EmailException {
		return userService.resetPasswordAndSendToEmail(user);
	}

	@PutMapping(value = "/status")
	@ApiOperation(value = "Altera status do user")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	public ResponseEntity<HttpStatus> updateUserStatus(@NotNull @RequestBody UserDTO userDTO) {
		return userService.updateUserStatus(userDTO);
	}

	@GetMapping(value = "/all")
	@ApiOperation(value = "Método retorna todos users")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	public List<UserDTO> getAllUsers() {
		return userService.getAllUsers();
	}

	@DeleteMapping(value = "/{userId}")
	@ApiOperation(value = "Método que deleta o user utilizador pelo ID")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	public ResponseEntityCustom deleteUserById(@PathVariable Long userId) throws UsernameNotFoundException {
		return userService.deleteUserById(userId);
	}
}
