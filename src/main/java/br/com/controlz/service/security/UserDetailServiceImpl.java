package br.com.controlz.service.security;

import br.com.controlz.domain.entity.security.User;
import br.com.controlz.domain.enums.RoleEnum;
import br.com.controlz.domain.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserDetailServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;

	public UserDetailServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
		Optional<User> user = userRepository.findByName(name);
		UserSpringSecurityService userSpringSecurityService = new UserSpringSecurityService();
		if (user.isEmpty()) {
			throw new UsernameNotFoundException("User não encontrado na base");
		}
		String authorities = applyAuthorities(user.get());
		userSpringSecurityService.setId(user.get().getIdUser());
		userSpringSecurityService.setName(user.get().getName());
		userSpringSecurityService.setPassword(user.get().getPassword());
		userSpringSecurityService.setStatus(user.get().getStatus());
		userSpringSecurityService.setAuthorities(List.of(new SimpleGrantedAuthority(authorities)));
		return userSpringSecurityService;
	}

	private String applyAuthorities(User user) {
		String authority = null;
		Long idRole = user.getIdRole();
		if (RoleEnum.ADMIN.getCod().equals(idRole)) {
			authority = RoleEnum.ADMIN.getDescription();
		} else if (RoleEnum.MANAGER.getCod().equals(idRole)) {
			authority = RoleEnum.MANAGER.getDescription();
		}
		return authority;
	}
}

