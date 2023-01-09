package br.com.controlz.service;

import br.com.controlz.domain.entity.security.User;
import br.com.controlz.domain.exception.EmailException;
import br.com.controlz.domain.exception.EmailNotFoundException;
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
	private final MailBuildService mailBuildService;

	public AuthService(UserRepository userRepository,
					   BCryptPasswordEncoder bCryptPasswordEncoder,
					   MailBuildService mailBuildService) {
		this.userRepository = userRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.mailBuildService = mailBuildService;
	}

	public void generationPasswordAndSend(String email) throws UsernameNotFoundException, EmailException {
		if (!EmailUtils.isEmailPatternValid(email)) {
			throw new EmailException("Email inválido");
		}
		User user = userRepository.findByEmail(email).orElseThrow(
				() -> new UsernameNotFoundException("User não encontrado na base"));
		String newPassword = TokenAndPasswordUtils.generateNewPassword();
		user.setPassword(bCryptPasswordEncoder.encode(newPassword));
		mailBuildService.newSendPasswordEmail(user, newPassword);
		userRepository.save(user);
	}

	public void changePassword(String email, String password) throws EmailException, EmailNotFoundException {
		if (!EmailUtils.isEmailPatternValid(email)) {
			throw new EmailException("Email inválido");
		}
		Optional<User> user = userRepository.findByEmail(email);
		if (user.isEmpty()) {
			throw new EmailNotFoundException("Email não encontrado na base");
		}
		user.get().setPassword(bCryptPasswordEncoder.encode(password));
		userRepository.save(user.get());
	}
}
