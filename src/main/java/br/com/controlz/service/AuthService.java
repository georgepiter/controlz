package br.com.controlz.service;

import br.com.controlz.domain.entity.security.User;
import br.com.controlz.domain.exception.EmailException;
import br.com.controlz.domain.repository.UserRepository;
import br.com.controlz.utils.EmailUtils;
import br.com.controlz.utils.TokenAndPasswordUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	public AuthService(UserRepository userRepository,
					   BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userRepository = userRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	public void newPassword(String email) throws UsernameNotFoundException, EmailException {
		if (!EmailUtils.isEmailPatternValid(email)) {
			throw new EmailException("Email inválido");
		}
		User user = userRepository.findByEmail(email).orElseThrow(
				() -> new UsernameNotFoundException("User não encontrado na base"));
		String newPassword = TokenAndPasswordUtils.generateNewPassword();
		user.setPassword(bCryptPasswordEncoder.encode(newPassword));
		userRepository.save(user);
	}

	public void changePassword(String email, String password) throws EmailException {
		if (!EmailUtils.isEmailPatternValid(email)) {
			throw new EmailException("Email inválido");
		}
		Optional<User> user = userRepository.findByEmail(email);
		if (user.isEmpty()) {
			throw new EmailException("Email não encontrado na base");
		}
		user.get().setPassword(bCryptPasswordEncoder.encode(password));
		userRepository.save(user.get());
	}
}
