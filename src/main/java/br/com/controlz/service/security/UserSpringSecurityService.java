package br.com.controlz.service.security;

import br.com.controlz.domain.entity.security.Role;
import br.com.controlz.domain.enums.StatusEnum;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;

@Service
public class UserSpringSecurityService implements UserDetails {

	private Long id;
	private String email;
	private String name;
	private String password;
	private Collection<? extends GrantedAuthority> authorities;
	private Integer status;

	public UserSpringSecurityService() {
	}

	public UserSpringSecurityService(Long id, String email, String name, String password, Set<Role> role, Integer status) {
		this.id = id;
		this.email = email;
		this.name = name;
		this.password = password;
		this.status = status;
		this.authorities = role.stream().map(x -> new SimpleGrantedAuthority(x.getName())).toList();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return status == StatusEnum.ACTIVE.getValue();
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return name;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setAuthorities(Collection<? extends GrantedAuthority> role) {
		this.authorities = role;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
