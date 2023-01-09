package br.com.controlz.service;

import br.com.controlz.domain.dto.UserDTO;
import br.com.controlz.domain.entity.security.User;
import br.com.controlz.domain.enums.RoleEnum;
import br.com.controlz.domain.enums.StatusEnum;
import br.com.controlz.domain.exception.EmailException;
import br.com.controlz.domain.exception.EmailNotFoundException;
import br.com.controlz.domain.exception.EmailSenderException;
import br.com.controlz.domain.exception.UserException;
import br.com.controlz.domain.repository.UserRepository;
import br.com.controlz.utils.EmailUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

	public ResponseEntity<HttpStatus> registerNewUser(UserDTO userDTO) throws EmailException, UserException {
		if (!EmailUtils.isEmailPatternValid(userDTO.getEmail())) {
			throw new EmailException("O Email não está no formato válido");
		}
		List<User> allUsers = userRepository.findAll()
				.stream().filter(user -> user.getName().equals(userDTO.getName())
						|| user.getEmail().equals(userDTO.getEmail())).toList();
		if (!allUsers.isEmpty()) {
			throw new UserException("Usuário já cadastrado com nome ou e-mail");
		}
		User newUser = new User.Builder()
				.name(userDTO.getName())
				.email(userDTO.getEmail())
				.idRole(RoleEnum.MANAGER.getCod())
				.status(StatusEnum.ACTIVE.getValue())
				.createTime(LocalDateTime.now())
				.password(passwordEncoder.encode(userDTO.getPassword()))
				.createNewUser();
		userRepository.save(newUser);
		return ResponseEntity.ok(HttpStatus.OK);
	}

	public ResponseEntity<HttpStatus> resetPassword(UserDTO user) throws EmailException, EmailNotFoundException {
		authService.changePassword(user.getEmail(), user.getPassword());
		return ResponseEntity.ok(HttpStatus.OK);
	}

	public ResponseEntity<HttpStatus> deleteUserById(Long idUser) {
		Optional<User> user = userRepository.findById(idUser);
		if (user.isEmpty()) {
			throw new UsernameNotFoundException("User não encontrado pelo ID na base");
		}
		userRepository.delete(user.get());
		return ResponseEntity.ok(HttpStatus.OK);
	}

	public ResponseEntity<HttpStatus> resetPasswordAndSendToEmail(UserDTO user) throws EmailException, UsernameNotFoundException, EmailSenderException {
		authService.generationPasswordAndSend(user.getEmail());
		return ResponseEntity.ok(HttpStatus.OK);
	}
}
