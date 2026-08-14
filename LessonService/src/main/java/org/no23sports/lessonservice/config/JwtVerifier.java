package org.no23sports.lessonservice.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
 

@Component
public class JwtVerifier {
 
	@Value("${jwt.secret}")
	private String secret;
 
	private SecretKey getKey() throws IOException {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
	}
 
	
	public Claims verifyAndExtract(String token) throws IOException {
		return Jwts.parser()
				.verifyWith(getKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}
 
	public String extractEmail(Claims claims) {
		return claims.getSubject();
	}
 
	public String extractRole(Claims claims) {
		return claims.get("role", String.class);
	}
 
	public String extractNameSurname(Claims claims) {
		return claims.get("nameSurname", String.class);
	}
}