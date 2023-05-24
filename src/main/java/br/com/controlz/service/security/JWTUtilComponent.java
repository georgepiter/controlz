package br.com.controlz.service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtilComponent {

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private String expiration;

	public String generateToken(String username, Long userId, String authority, String email) {
		return Jwts.builder()
				.setSubject(username)
				.setExpiration(new Date(System.currentTimeMillis() + expiration))
				.claim("name", username)
				.claim("email", email)
				.claim("userId", userId)
				.claim("nameApp", "ControlZ")
				.claim("role", authority)
				.signWith(SignatureAlgorithm.HS512, secret.getBytes(StandardCharsets.UTF_8))
				.compact();
	}

	public Boolean isTokenValid(String token) {
		Claims claims = getClaims(token);
		if (claims != null) {
			String username = claims.getSubject();
			Date expirationDate = claims.getExpiration();
			Date dateNow = new Date(System.currentTimeMillis());
			return username != null && expirationDate != null && dateNow.before(expirationDate);
		}
		return false;
	}

	public String getUserName(String token) {
		Claims claims = getClaims(token);
		if (claims != null) {
			return claims.getSubject();
		}
		return null;
	}

	public Claims getClaims(String token) {
		try {
			return Jwts.parser()
					.setSigningKey(secret.getBytes(StandardCharsets.UTF_8))
					.parseClaimsJws(token)
					.getBody();
		} catch (Exception e) {
			return null;
		}
	}
}
