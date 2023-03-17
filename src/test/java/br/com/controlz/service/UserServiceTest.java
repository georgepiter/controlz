
package br.com.controlz.service;

import br.com.controlz.domain.dto.ResponseEntityCustom;
import br.com.controlz.domain.dto.UserDTO;
import br.com.controlz.domain.entity.security.User;
import br.com.controlz.domain.enums.RoleEnum;
import br.com.controlz.domain.enums.StatusEnum;
import br.com.controlz.domain.exception.UserException;
import br.com.controlz.domain.repository.UserRepository;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para UserService")
class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private BCryptPasswordEncoder passwordEncoder;

	@InjectMocks
	private UserService userService;

	@BeforeEach
	public void setUp() {
		userService = new UserService(this.passwordEncoder, this.userRepository);
	}

	@Test
	@DisplayName("Testa o cadastro de um novo usuário com sucesso")
	void testRegisterNewUserSuccess() throws UserException {

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
	void testRegisterNewUserInvalidEmail() throws UserException {

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
	void testRegisterNewUserExistingEmailAndName() throws UserException {

		// given
		UserDTO userDTO = new UserDTO.Builder()
				.name("teste")
				.email("teste@gmail.com")
				.password("123")
				.roleId(1L)
				.createNewUser();
		given(userRepository.existsByNameOrEmail(userDTO.getName(), userDTO.getEmail())).willReturn(true);

		// when
		ResponseEntityCustom response = userService.registerNewUser(userDTO);//todo ajustar

		// then
		assertEquals(HttpStatus.CONFLICT.value(), response.getStatus());
		assertEquals("Usuário já existe com o nome ou email especificado", response.getMessage());
		verify(userRepository, times(0)).save(any(User.class));
	}

	@Test
	@DisplayName("Testa a atualização do status de um usuário existente e ativo")
	void testUpdateActiveUserStatus() {

		// given
		Long userId = 1L;
		UserDTO userDTO = new UserDTO.Builder()
				.userId(userId)
				.status(StatusEnum.ACTIVE.getLabel())
				.createNewUser();

		User user = new User.Builder()
				.userId(userId)
				.status(StatusEnum.INACTIVE.getValue())
				.createNewUser();
		when(userRepository.findById(userId)).thenReturn(Optional.of(user));

		// when
		ResponseEntity<HttpStatus> response = userService.updateUserStatus(userDTO);

		// then
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(StatusEnum.ACTIVE.getValue(), user.getStatus());
		verify(userRepository, times(1)).save(user);
	}

	@Test
	@DisplayName("Testa a atualização do status de um usuário existente e inativo")
	void testUpdateInactiveUserStatus() {
		// given
		Long userId = 1L;
		UserDTO userDTO = new UserDTO.Builder()
				.userId(userId)
				.status(StatusEnum.INACTIVE.getLabel())
				.createNewUser();

		User user = new User.Builder()
				.userId(userId)
				.status(StatusEnum.ACTIVE.getValue())
				.createNewUser();
		when(userRepository.findById(userId)).thenReturn(Optional.of(user));

		// when
		ResponseEntity<HttpStatus> response = userService.updateUserStatus(userDTO);

		// then
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(StatusEnum.INACTIVE.getValue(), user.getStatus());
		verify(userRepository, times(1)).save(user);
	}

	@Test
	@DisplayName("Testa a falha ao atualizar o status de um usuário inexistente")
	void testUpdateNonExistingUserStatus() {
		// given
		Long userId = 1L;
		UserDTO userDTO = new UserDTO.Builder()
				.userId(userId)
				.status(StatusEnum.ACTIVE.getLabel())
				.createNewUser();

		when(userRepository.findById(userId)).thenReturn(Optional.empty());

		// when/then
		assertThrows(UsernameNotFoundException.class, () -> userService.updateUserStatus(userDTO));
		verify(userRepository, never()).save(any(User.class));
	}

	@Test
	@DisplayName("Testa se a lista de usuários é retornada corretamente")
	void testGetAllUsers() {
		// given
		List<User> userList = Arrays.asList(
				new User.Builder()
						.email("user1@test.com")
						.name("User 1")
						.userId(1L)
						.roleId(RoleEnum.ADMIN.getCod())
						.status(StatusEnum.ACTIVE.getValue())
						.createNewUser(),
				new User.Builder()
						.email("user2@test.com")
						.name("User 2")
						.userId(2L)
						.roleId(RoleEnum.MANAGER.getCod())
						.status(StatusEnum.INACTIVE.getValue())
						.createNewUser()
		);
		when(userRepository.findAll()).thenReturn(userList);

		// when
		List<UserDTO> userDTOList = userService.getAllUsers();

		// then
		assertEquals(userList.size(), userDTOList.size());

		for (int i = 0; i < userList.size(); i++) {
			User user = userList.get(i);
			UserDTO userDTO = userDTOList.get(i);

			assertEquals(user.getEmail(), userDTO.getEmail());
			assertEquals(user.getName(), userDTO.getName());
			assertEquals(user.getUserId(), userDTO.getUserId());
			assertEquals(user.getRoleId(), userDTO.getRoleId());
			assertEquals(user.getStatus().equals(StatusEnum.ACTIVE.getValue()) ? StatusEnum.ACTIVE.getLabel() : StatusEnum.INACTIVE.getLabel(), userDTO.getStatus());
			assertEquals(user.getRoleId().equals(RoleEnum.ADMIN.getCod()) ? RoleEnum.ADMIN.getDescription() : RoleEnum.MANAGER.getDescription(), userDTO.getPerfil());
		}
	}

	@Test
	@DisplayName("Testa se uma lista vazia é retornada quando não há usuários")
	void testGetAllUsersEmptyList() {
		// given
		when(userRepository.findAll()).thenReturn(Collections.emptyList());

		// when
		List<UserDTO> userDTOList = userService.getAllUsers();

		// then
		assertTrue(userDTOList.isEmpty());
	}

	@Test
	@DisplayName("Teste deletar um usuário existente")
	void testDeleteExistingUser() {
		// given
		Long userId = 1L;
		User user = new User.Builder()
				.userId(userId)
				.name("John Doe")
				.email("john.doe@example.com")
				.password("password123")
				.roleId(RoleEnum.MANAGER.getCod())
				.status(StatusEnum.ACTIVE.getValue())
				.createNewUser();

		when(userRepository.findById(userId)).thenReturn(Optional.of(user));

		// when
		ResponseEntityCustom response = userService.deleteUserById(userId);

		// then
		verify(userRepository, times(1)).delete(user);
		assertEquals(HttpStatus.NO_CONTENT.value() , response.getStatus());
		assertEquals("Usuário deletado com sucesso!", response.getMessage());
	}

	@Test
	@DisplayName("Teste deletar um usuário inexistente")
	void testDeleteNonExistingUser() {
		// given
		Long userId = 1L;
		when(userRepository.findById(userId)).thenReturn(Optional.empty());

		// when
		assertThrows(UsernameNotFoundException.class, () -> userService.deleteUserById(userId));

		// then
		verify(userRepository, times(0)).delete(any(User.class));
	}

}
