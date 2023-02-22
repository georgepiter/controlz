package br.com.controlz.service;

import br.com.controlz.domain.entity.security.User;
import br.com.controlz.domain.exception.EmailException;
import br.com.controlz.domain.exception.EmailNotFoundException;
import br.com.controlz.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para AuthService")
class AuthServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private BCryptPasswordEncoder passwordEncoder;

	@Mock
	private MailBuildService mailBuildService;

	@InjectMocks
	private AuthService authService;

	@BeforeEach
	public void setUp() {
		authService = new AuthService(this.userRepository, this.passwordEncoder, this.mailBuildService);
	}

	@Test
	@DisplayName("Testa a geração de uma nova senha para um usuário existente")
	void testGenerationPasswordAndSendExistingUser() throws UsernameNotFoundException, EmailException {
		// given
		String email = "teste@terra.com.br";

		User user = new User.Builder()
				.email(email)
				.name("teste")
				.password("oldPassword")
				.createNewUser();

		when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

		// when
		ResponseEntity<HttpStatus> response = authService.generationPasswordAndSend(email);

		// then
		ArgumentCaptor<User> categoryCaptor = ArgumentCaptor.forClass(User.class);
		verify(userRepository).save(categoryCaptor.capture());
		assertEquals(HttpStatus.OK, response.getStatusCode());
		verify(userRepository, times(1)).save(user);
	}

	@Test
	@DisplayName("Testa a geração de uma nova senha para um email inválido")
	void testGenerationPasswordAndSendInvalidEmail() throws EmailException {
		// given
		String email = "testeterra.com.br";

		// when
		EmailException exception = assertThrows(EmailException.class,
				() -> authService.generationPasswordAndSend(email));

		// then
		assertEquals("Email inválido", exception.getMessage());
		verify(userRepository, times(0)).findByEmail(email);
		verify(mailBuildService, times(0)).newSendPasswordEmail(any(User.class), anyString());
		verify(userRepository, times(0)).save(any(User.class));
	}

	@Test
	@DisplayName("Testa a geração de uma nova senha para um usuário inexistente")
	void testGenerationPasswordAndSendNonExistingUser() throws EmailException {
		// given
		String email = "teste@gmail.com";
		when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

		// when
		UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
				() -> authService.generationPasswordAndSend(email));

		// then
		assertEquals("Email não cadastrado na base", exception.getMessage());
		verify(mailBuildService, times(0)).newSendPasswordEmail(any(User.class), anyString());
		verify(userRepository, times(0)).save(any(User.class));
	}

	@Test
	@DisplayName("Testa a troca de senha de um usuário existente")
	void testChangePasswordExistingUser() throws EmailException, EmailNotFoundException {

		// given
		String email = "teste@terra.com.br";
		String password = "newPassword";
		User user = new User.Builder()
				.email(email)
				.password("oldPassword")
				.createNewUser();

		when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
		String encodedPassword = "encodedPassword";
		when(passwordEncoder.encode(password)).thenReturn(encodedPassword);

		// when
		ResponseEntity<HttpStatus> response = authService.changePassword(email, password);

		// then
		assertEquals(HttpStatus.OK, response.getStatusCode());
		verify(userRepository, times(1)).save(user);
		assertEquals(encodedPassword, user.getPassword(), "A senha do usuário deve estar codificada");
	}

	@Test
	@DisplayName("Testa a troca de senha de um usuário inexistente")
	void testChangePasswordNonExistingUser() {

		// given
		String email = "teste@mail.com";
		String password = "newPassword";
		when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

		// when
		EmailNotFoundException exception = assertThrows(EmailNotFoundException.class,
				() -> authService.changePassword(email, password));

		// then
		assertEquals("Email não encontrado na base", exception.getMessage());
		verify(userRepository, times(0)).save(any(User.class));
	}

	@Test
	@DisplayName("Testa a troca de senha com email inválido")
	void testChangePasswordInvalidEmail() {

		// given
		String email = "invalidemail";
		String password = "newpassword";

		// when
		EmailException exception = assertThrows(EmailException.class,
				() -> authService.changePassword(email, password));

		// then
		assertEquals("Email inválido", exception.getMessage());
		verify(userRepository, times(0)).findByEmail(email);
		verify(userRepository, times(0)).save(any(User.class));
	}

}
