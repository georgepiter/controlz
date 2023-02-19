package br.com.controlz.service;

import br.com.controlz.domain.dto.ResponseEntityCustom;
import br.com.controlz.domain.dto.UserDTO;
import br.com.controlz.domain.entity.security.User;
import br.com.controlz.domain.enums.RoleEnum;
import br.com.controlz.domain.enums.StatusEnum;
import br.com.controlz.domain.repository.UserRepository;
import br.com.controlz.utils.EmailUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

	private final BCryptPasswordEncoder passwordEncoder;
	private final UserRepository userRepository;

	public UserService(BCryptPasswordEncoder passwordEncoder,
					   UserRepository userRepository) {
		this.passwordEncoder = passwordEncoder;
		this.userRepository = userRepository;
	}

	public ResponseEntityCustom registerNewUser(UserDTO userDTO) {
		if (!EmailUtils.isValidEmailFormat(userDTO.getEmail())) {
			return new ResponseEntityCustom(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, "O formato do email é inválido");
		}
		if (userRepository.existsByNameOrEmail(userDTO.getName(), userDTO.getEmail())) {
			return new ResponseEntityCustom(HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT, "Usuário já existe com o nome ou email especificado");
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
		return new ResponseEntityCustom(HttpStatus.CREATED.value(), HttpStatus.CREATED, "Usuário criado com sucesso");
	}

	public ResponseEntityCustom deleteUserById(Long idUser) {
		User user = userRepository.findById(idUser).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado pelo ID na base"));
		userRepository.delete(user);
		return new ResponseEntityCustom(HttpStatus.NO_CONTENT.value(), HttpStatus.NO_CONTENT, "Usuário deletado com sucesso!");
	}

	public List<UserDTO> getAllUsers() {
		List<User> users = userRepository.findAll();
		return users.stream()
				.map(user -> {
					String status = (user.getStatus().equals(StatusEnum.ACTIVE.getValue()))
							? StatusEnum.ACTIVE.getLabel() : StatusEnum.INACTIVE.getLabel();

					String perfil = (user.getRoleId().equals(RoleEnum.ADMIN.getCod()))
							? RoleEnum.ADMIN.getDescription() : RoleEnum.MANAGER.getDescription();

					return new UserDTO.Builder()
							.email(user.getEmail())
							.name(user.getName())
							.userId(user.getUserId())
							.roleId(user.getRoleId())
							.status(status)
							.perfil(perfil)
							.createNewUser();
				})
				.toList();
	}

	public ResponseEntity<HttpStatus> updateUserStatus(UserDTO userDTO) {
		User user = userRepository.findById(userDTO.getUserId()).orElseThrow(() -> new UsernameNotFoundException("User não encontrado na base"));
		user.setUserId(userDTO.getUserId());
		user.setStatus(userDTO.getStatus().equals(StatusEnum.ACTIVE.getLabel()) ? StatusEnum.ACTIVE.getValue() : StatusEnum.INACTIVE.getValue());
		userRepository.save(user);
		return ResponseEntity.ok(HttpStatus.OK);
	}
}
