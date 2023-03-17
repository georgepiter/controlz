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

@Component
public class UserDetailServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;

	public UserDetailServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
		User user = userRepository.findByName(name).orElseThrow(() -> new UsernameNotFoundException("User não encontrado na base"));

		UserSpringSecurityService userSpringSecurityService = new UserSpringSecurityService();
		String authorities = applyAuthorities(user);
		userSpringSecurityService.setId(user.getUserId());
		userSpringSecurityService.setName(user.getName());
		userSpringSecurityService.setEmail(user.getEmail());
		userSpringSecurityService.setPassword(user.getPassword());
		userSpringSecurityService.setStatus(user.getStatus());
		userSpringSecurityService.setAuthorities(List.of(new SimpleGrantedAuthority(authorities)));
		return userSpringSecurityService;
	}

	private String applyAuthorities(User user) {
		String authority = null;
		Long idRole = user.getRoleId();
		if (RoleEnum.ADMIN.getCod().equals(idRole)) {
			authority = RoleEnum.ADMIN.getDescription();
		} else if (RoleEnum.MANAGER.getCod().equals(idRole)) {
			authority = RoleEnum.MANAGER.getDescription();
		}
		return authority;
	}
}

