package br.com.controlz.service.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final UserDetailsService userDetailsService;
	private final JWTUtilComponent JWTUtilComponent;

	private static final String[] PUBLIC_MATCHERS = {"/api/v1/email**"};
	private static final String[] SWAGGER_MATCHERS = {"/swagger-resources/*", "*.html", "/api/v1/swagger.json"};

	public SecurityConfig(UserDetailsService userDetailsService,
						  JWTUtilComponent JWTUtilComponent) {
		this.userDetailsService = userDetailsService;
		this.JWTUtilComponent = JWTUtilComponent;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable(); //todo antes de subir em prod disable em csrf
		http.authorizeRequests().antMatchers(PUBLIC_MATCHERS).permitAll();
		http.authorizeRequests().antMatchers(SWAGGER_MATCHERS).hasRole("ADMIN").anyRequest().permitAll();
		http.addFilter(new JWTAuthenticationFilter(authenticationManager(), JWTUtilComponent));
		http.addFilter(new JWTAuthorizationFilter(authenticationManager(), JWTUtilComponent, userDetailsService));
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(encoderPassword());
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration(" /**", new CorsConfiguration().applyPermitDefaultValues());
		return source;
	}

	@Bean
	public BCryptPasswordEncoder encoderPassword() {
		return new BCryptPasswordEncoder();
	}

}
