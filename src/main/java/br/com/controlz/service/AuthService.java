package br.com.controlz.service;

import br.com.controlz.domain.dto.auth.AuthenticationRequestDTO;
import br.com.controlz.domain.dto.auth.AuthenticationResponseDTO;
import br.com.controlz.domain.entity.security.User;
import br.com.controlz.domain.exception.AuthInvalidException;
import br.com.controlz.domain.exception.EmailException;
import br.com.controlz.domain.exception.EmailNotFoundException;
import br.com.controlz.domain.repository.UserRepository;
import br.com.controlz.service.security.JWTUtilComponent;
import br.com.controlz.service.security.UserSpringSecurityService;
import br.com.controlz.utils.EmailUtils;
import br.com.controlz.utils.PasswordUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class AuthService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final MailBuildService mailBuildService;

	private final AuthenticationManager authenticationManager;
	private final JWTUtilComponent jwtUtilComponent;

	public AuthService(UserRepository userRepository,
	                   BCryptPasswordEncoder bCryptPasswordEncoder,
	                   MailBuildService mailBuildService,
	                   AuthenticationManager authenticationManager,
	                   JWTUtilComponent jwtUtilComponent) {
		this.userRepository = userRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.mailBuildService = mailBuildService;
		this.authenticationManager = authenticationManager;
		this.jwtUtilComponent = jwtUtilComponent;
	}

	public ResponseEntity<HttpStatus> generationPasswordAndSend(String email) throws EmailException {
		if (!EmailUtils.isValidEmailFormat(email)) {
			throw new EmailException("Email inválido");
		}
		User user = userRepository.findByEmail(email).orElseThrow(
				() -> new UsernameNotFoundException("Email não cadastrado na base"));

		String newPassword = PasswordUtils.generateNewPassword();
		user.setPassword(bCryptPasswordEncoder.encode(newPassword));
		mailBuildService.newSendPasswordEmail(user, newPassword);
		userRepository.save(user);
		return ResponseEntity.ok(HttpStatus.OK);
	}

	public ResponseEntity<HttpStatus> changePassword(String email, String password) throws EmailException, EmailNotFoundException {
		if (!EmailUtils.isValidEmailFormat(email)) {
			throw new EmailException("Email inválido");
		}
		Optional<User> user = userRepository.findByEmail(email);
		if (user.isEmpty()) {
			throw new EmailNotFoundException("Email não encontrado na base");
		}
		user.get().setPassword(bCryptPasswordEncoder.encode(password));
		userRepository.save(user.get());
		return ResponseEntity.ok(HttpStatus.OK);
	}

	public ResponseEntity<?> getToken(AuthenticationRequestDTO request) {

		try {
			Authentication authenticate = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword()));

			String username = ((UserSpringSecurityService) authenticate.getPrincipal()).getUsername();
			String email = ((UserSpringSecurityService) authenticate.getPrincipal()).getEmail();
			Long userId = ((UserSpringSecurityService) authenticate.getPrincipal()).getId();
			String authority = Objects.requireNonNull(((UserSpringSecurityService) authenticate.getPrincipal()).getAuthorities()
					.stream().findFirst().orElse(null)).getAuthority();

			String token = jwtUtilComponent.generateToken(username, userId, authority, email);
			AuthenticationResponseDTO response = new AuthenticationResponseDTO(token);
			return ResponseEntity.ok(response);
		} catch (AuthenticationException e) {
			throw new AuthInvalidException("Nome de usuário ou senha inválida");
		}
	}
}
