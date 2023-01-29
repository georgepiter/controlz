package br.com.controlz.service;

import br.com.controlz.domain.dto.ResponseEntityCustom;
import br.com.controlz.domain.dto.UserDTO;
import br.com.controlz.domain.entity.security.User;
import br.com.controlz.domain.enums.RoleEnum;
import br.com.controlz.domain.enums.StatusEnum;
import br.com.controlz.domain.exception.EmailException;
import br.com.controlz.domain.exception.EmailNotFoundException;
import br.com.controlz.domain.exception.UserException;
import br.com.controlz.domain.repository.UserRepository;
import br.com.controlz.utils.EmailUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

	private final BCryptPasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	private final AuthService authService;

	public UserService(BCryptPasswordEncoder passwordEncoder,
					   UserRepository userRepository,
					   AuthService authService) {
		this.passwordEncoder = passwordEncoder;
		this.userRepository = userRepository;
		this.authService = authService;
	}

	public ResponseEntityCustom registerNewUser(UserDTO userDTO) throws EmailException, UserException {
		if (!EmailUtils.isEmailPatternValid(userDTO.getEmail())) {
			throw new EmailException("O Email não está nem um formato válido");
		}

		List<User> allUsers = userRepository.findAll()
				.stream().filter(user -> user.getName().equals(userDTO.getName())
						|| user.getEmail().equals(userDTO.getEmail())).toList();

		if (!allUsers.isEmpty()) {
			throw new UserException("Usuário já cadastrado com nome ou e-mail digitado");
		}

		User newUser = new User.Builder()
				.name(userDTO.getName())
				.email(userDTO.getEmail())
				.roleId(userDTO.getRoleId())
				.status(StatusEnum.ACTIVE.getValue())
				.createTime(LocalDateTime.now())
				.password(passwordEncoder.encode(userDTO.getPassword()))
				.createNewUser();
		userRepository.save(newUser);
		return new ResponseEntityCustom(HttpStatus.CREATED.value(), HttpStatus.CREATED, "Usuário criado com sucesso!");
	}

	public ResponseEntity<HttpStatus> resetPassword(UserDTO user) throws EmailException, EmailNotFoundException {
		authService.changePassword(user.getEmail(), user.getPassword());
		return ResponseEntity.ok(HttpStatus.OK);
	}

	public ResponseEntityCustom deleteUserById(Long idUser) {
		Optional<User> user = userRepository.findById(idUser);
		if (user.isEmpty()) {
			throw new UsernameNotFoundException("Usuário não encontrado pelo ID na base");
		}
		userRepository.delete(user.get());
		return new ResponseEntityCustom(HttpStatus.NO_CONTENT.value(), HttpStatus.NO_CONTENT, "Usuário deletado com sucesso!");
	}

	public ResponseEntity<HttpStatus> resetPasswordAndSendToEmail(UserDTO user) throws EmailException, UsernameNotFoundException {
		authService.generationPasswordAndSend(user.getEmail());
		return ResponseEntity.ok(HttpStatus.OK);
	}

	public List<UserDTO> getAllUsers() {
		List<User> users = userRepository.findAll();
		List<UserDTO> userDTOS = new ArrayList<>();
		users.forEach(
				user -> {
					UserDTO newUser = new UserDTO.Builder()
							.email(user.getEmail())
							.name(user.getName())
							.userId(user.getUserId())
							.roleId(user.getRoleId())
							.status(user.getStatus().equals(StatusEnum.ACTIVE.getValue()) ? StatusEnum.ACTIVE.getLabel() : StatusEnum.INACTIVE.getLabel())
							.perfil(user.getRoleId().equals(RoleEnum.ADMIN.getCod()) ? RoleEnum.ADMIN.getDescription() : RoleEnum.MANAGER.getDescription())
							.createNewUser();
					userDTOS.add(newUser);
				}
		);
		return userDTOS;
	}

	public ResponseEntity<HttpStatus> updateUserStatus(UserDTO userDTO) {
		Optional<User> user = userRepository.findById(userDTO.getUserId());
		if (user.isEmpty()) {
			throw new UsernameNotFoundException("User não encontrado na base");
		}
		user.get().setUserId(userDTO.getUserId());
		user.get().setStatus(userDTO.getStatus().equals(StatusEnum.ACTIVE.getLabel()) ? StatusEnum.ACTIVE.getValue() : StatusEnum.INACTIVE.getValue());
		userRepository.save(user.get());
		return ResponseEntity.ok(HttpStatus.OK);
	}
}
