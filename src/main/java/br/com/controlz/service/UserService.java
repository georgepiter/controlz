package br.com.controlz.service;

import br.com.controlz.domain.enums.RoleEnum;
import br.com.controlz.domain.enums.StatusEnum;
import br.com.controlz.domain.dto.UserDto;
import br.com.controlz.domain.entity.security.User;
import br.com.controlz.domain.exception.EmailException;
import br.com.controlz.domain.exception.UserException;
import br.com.controlz.domain.repository.UserRepository;
import br.com.controlz.utils.EmailUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

	private final BCryptPasswordEncoder passwordEncoder;
	private final UserRepository userRepository;

	public UserService(BCryptPasswordEncoder passwordEncoder,
					   UserRepository userRepository) {
		this.passwordEncoder = passwordEncoder;
		this.userRepository = userRepository;
	}

	public ResponseEntity<HttpStatus> registerNewUser(UserDto userDTO) throws EmailException, UserException {
		if (!EmailUtils.isEmailPatternValid(userDTO.getEmail())) {
			throw new EmailException("O Email não está no formato válido");
		}
		List<User> allUsers = userRepository.findAll()
				.stream().filter(user -> user.getName().equals(userDTO.getName())
						|| user.getEmail().equals(userDTO.getEmail())).collect(Collectors.toList());
		if (!allUsers.isEmpty()) {
			throw new UserException("Usuário já cadastrado com nome ou e-mail");
		}
		User newUser = new User.Builder()
				.name(userDTO.getName())
				.email(userDTO.getEmail())
				.idRole(RoleEnum.MANAGER.getCod())
				.status(StatusEnum.ACTIVE.getValue())
				.password(passwordEncoder.encode(userDTO.getPassword()))
				.createNewUser();
		userRepository.save(newUser);
		return ResponseEntity.ok(HttpStatus.OK);
	}
}
