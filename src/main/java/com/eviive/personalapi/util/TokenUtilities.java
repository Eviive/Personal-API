package com.eviive.personalapi.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public final class TokenUtilities {
	
	private static final Algorithm algorithm = Algorithm.HMAC256(System.getenv("secret-jwt-key").getBytes());
	
	private TokenUtilities() {
		throw new UnsupportedOperationException("Utility class cannot be instantiated");
	}
	
	public static String generateAccessToken(String subject, String issuer, List<String> claims) {
		return JWT.create()
				  .withSubject(subject)
				  .withExpiresAt(Date.valueOf(LocalDate.now().plusDays(1)))
				  .withIssuer(issuer)
				  .withClaim("roles", claims)
				  .sign(algorithm);
	}
	
	public static String generateRefreshToken(String subject, String issuer) {
		return JWT.create()
				  .withSubject(subject)
				  .withExpiresAt(Date.valueOf(LocalDate.now().plusDays(3)))
				  .withIssuer(issuer)
				  .sign(algorithm);
	}
	
	public static DecodedJWT verifyToken(String token) {
		JWTVerifier verifier = JWT.require(algorithm).build();
		return verifier.verify(token);
	}
	
}