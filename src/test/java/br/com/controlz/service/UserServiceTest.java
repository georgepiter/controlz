
package br.com.controlz.service;

import br.com.controlz.domain.dto.ResponseEntityCustom;
import br.com.controlz.domain.dto.UserDTO;
import br.com.controlz.domain.entity.security.User;
import br.com.controlz.domain.enums.StatusEnum;
import br.com.controlz.domain.exception.EmailException;
import br.com.controlz.domain.exception.EmailNotFoundException;
import br.com.controlz.domain.repository.UserRepository;
import br.com.controlz.utils.PasswordUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private BCryptPasswordEncoder passwordEncoder;

	@Mock
	private AuthService authService;

	@Mock
	private MailBuildService mailBuildService;

	@InjectMocks
	private UserService userService;

	@BeforeEach
	public void setUp() {
		userService = new UserService(this.passwordEncoder, this.userRepository, this.authService);
	}

	@Test
	@DisplayName("Testa o cadastro de um novo usuário com sucesso")
	void testRegisterNewUserSuccess() {
		// given
		UserDTO userDTO = new UserDTO.Builder()
				.name("teste")
				.email("teste@gmail.com")
				.password("123")
				.roleId(1L)
				.createNewUser();
		given(userRepository.existsByNameOrEmail(userDTO.getName(), userDTO.getEmail())).willReturn(false);
		given(passwordEncoder.encode(userDTO.getPassword())).willReturn("123");
		given(userRepository.save(any(User.class))).willReturn(new User());

		// when
		ResponseEntityCustom response = userService.registerNewUser(userDTO);

		// then
		assertEquals(HttpStatus.CREATED.value(), response.getStatus());
		assertEquals("Usuário criado com sucesso", response.getMessage());
		verify(userRepository, times(1)).save(any(User.class));
	}

	@Test
	@DisplayName("Testa o cadastro de um novo usuário com email inválido")
	void testRegisterNewUserInvalidEmail() {
		// given
		UserDTO userDTO = new UserDTO.Builder()
				.name("teste")
				.email("testeegmail.com")
				.password("123")
				.roleId(1L)
				.createNewUser();

		// when
		ResponseEntityCustom response = userService.registerNewUser(userDTO);

		// then
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
		assertEquals("O formato do email é inválido", response.getMessage());
	}

	@Test
	@DisplayName("Testa o cadastro de um novo usuário com email e nome já existentes")
	void testRegisterNewUserExistingEmailAndName() {
		// given
		UserDTO userDTO = new UserDTO.Builder()
				.name("teste")
				.email("teste@gmail.com")
				.password("123")
				.roleId(1L)
				.createNewUser();
		given(userRepository.existsByNameOrEmail(userDTO.getName(), userDTO.getEmail())).willReturn(true);

		// when
		ResponseEntityCustom response = userService.registerNewUser(userDTO);

		// then
		assertEquals(HttpStatus.CONFLICT.value(), response.getStatus());
		assertEquals("Usuário já existe com o nome ou email especificado", response.getMessage());
		verify(userRepository, times(0)).save(any(User.class));
	}

	@Test
	@DisplayName("Testa o reset de senha de usuário com sucesso")
	void testResetPasswordSuccess() throws EmailException, EmailNotFoundException {
		// given
		String email = "test@gmail.com";
		String newPassword = "123";
		UserDTO userDTO = new UserDTO.Builder()
				.email(email)
				.password(newPassword)
				.createNewUser();

		// mockando o comportamento do método changePassword da authService
		doNothing().when(authService).changePassword(email, newPassword);

		// when
		ResponseEntity<HttpStatus> response = userService.resetPassword(userDTO);

		// then
		assertEquals(HttpStatus.OK, response.getStatusCode());
		verify(authService, times(1)).changePassword(email, newPassword);
	}

	@Test
	@DisplayName("Testa o envio de nova senha para um usuário existente")
	void testResetPasswordAndSendToEmailSuccess() throws EmailException, UsernameNotFoundException {
		// given
		String email = "teste@gmail.com";
		UserDTO userDTO = new UserDTO.Builder()
				.email(email)
				.createNewUser();
		User user = new User.Builder()
				.email(userDTO.getEmail())
				.password(passwordEncoder.encode("123"))
				.createTime(LocalDateTime.now())
				.status(StatusEnum.ACTIVE.getValue())
				.createNewUser();

		given(userRepository.findByEmail(email)).willReturn(Optional.of(user));
		given(PasswordUtils.generateNewPassword()).willReturn("1234");
		given(passwordEncoder.encode("1234")).willReturn("senhacodificada");
		doNothing().when(mailBuildService).newSendPasswordEmail(user, "1234");
		given(userRepository.save(user)).willReturn(user);

		// when
		ResponseEntity<HttpStatus> response = userService.resetPasswordAndSendToEmail(userDTO);

		// then
		assertEquals(HttpStatus.OK, response.getStatusCode());
		verify(userRepository, times(1)).findByEmail(email);
		verify(mailBuildService, times(1)).newSendPasswordEmail(user, "1234");
		assertEquals("senhacodificada", userDTO.getPassword());
	}



}
