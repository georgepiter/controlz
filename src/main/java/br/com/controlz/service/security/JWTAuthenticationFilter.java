package br.com.controlz.service.security;

import br.com.controlz.domain.dto.UserDTO;
import br.com.controlz.domain.exception.AuthInvalidException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;
	private final JWTUtilComponent JWTUtilComponent;

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager,
								   JWTUtilComponent JWTUtilComponent) {
		setAuthenticationFailureHandler(new JWTAuthenticationFailureHandler());
		this.authenticationManager = authenticationManager;
		this.JWTUtilComponent = JWTUtilComponent;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		try {
			UserDTO credentials = new ObjectMapper()
					.readValue(request.getInputStream(), UserDTO.class);

			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
					credentials.getName(),
					credentials.getPassword(),
					new ArrayList<>());
			return authenticationManager.authenticate(authToken);
		} catch (IOException e) {
			throw new AuthInvalidException(e.getMessage());
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request,
											HttpServletResponse response,
											FilterChain chain,
											Authentication authResult) throws IOException {

		String username = ((UserSpringSecurityService) authResult.getPrincipal()).getUsername();
		String token = JWTUtilComponent.generateToken(username);
		response.addHeader("Authorization", token);
		response.getWriter().write(token);
		response.getWriter().flush();
	}

	private static class JWTAuthenticationFailureHandler implements AuthenticationFailureHandler {

		@Override
		public void onAuthenticationFailure(HttpServletRequest request,
											HttpServletResponse response,
											AuthenticationException exception) throws IOException {
			response.setStatus(401);
			response.setContentType("application/json");
			response.getWriter().append(json());
		}

		private String json() {
			long date = new Date().getTime();
			return "{\"timestamp\": " + date + ", "
					+ "\"status\": 401, "
					+ "\"error\": \"Not authorized\", "
					+ "\"message\": \"Invalid credentials or disabled user\", "
					+ "\"path\": \"/login\"}";
		}
	}
}

