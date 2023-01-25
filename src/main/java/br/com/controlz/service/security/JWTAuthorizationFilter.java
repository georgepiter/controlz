package br.com.controlz.service.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

	private final JWTUtilComponent jwtUtilComponent;
	private final UserDetailsService userDetailsService;

	public JWTAuthorizationFilter(AuthenticationManager authenticationManager,
								  JWTUtilComponent jwtUtilComponent,
								  UserDetailsService userDetailsService) {
		super(authenticationManager);
		this.jwtUtilComponent = jwtUtilComponent;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request,
									HttpServletResponse response,
									FilterChain chain) throws IOException, ServletException {
		String authorization = request.getHeader("Authorization");
		if (authorization != null && authorization.startsWith("Bearer")) {
			UsernamePasswordAuthenticationToken auth = getAuthentication(authorization.substring(7));
			if (auth != null) {
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		}
		chain.doFilter(request, response);
	}

	private UsernamePasswordAuthenticationToken getAuthentication(String token) {
		if (Boolean.TRUE.equals(jwtUtilComponent.isTokenValid(token))) {
			String username = jwtUtilComponent.getUserName(token);
			UserDetails user = userDetailsService.loadUserByUsername(username);
			return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
		}
		return null;
	}
}
