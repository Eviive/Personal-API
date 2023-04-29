package com.eviive.personalapi.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;
import java.util.Date;
import java.util.List;

public final class TokenUtilities {

	private static Algorithm algorithm;
	private static boolean isProduction;

	private TokenUtilities() {
		throw new UnsupportedOperationException("Utility class cannot be instantiated");
	}

	@Value("jwt-secret-key")
	public static void setAlgorithm(String secret) {
		TokenUtilities.algorithm = Algorithm.HMAC256(secret);
	}

	@Value("is-production")
	public static void setIsProduction(boolean isProduction) {
		TokenUtilities.isProduction = isProduction;
	}

	public static String generateAccessToken(String subject, String issuer, List<String> claims) {
		int maxAge = 15 * 60; // expires in 15 minutes
		return JWT.create()
				  .withSubject(subject)
				  .withExpiresAt(Date.from(Instant.now().plusSeconds(maxAge)))
				  .withIssuer(issuer)
				  .withClaim("roles", claims)
				  .sign(algorithm);
	}

	public static Cookie generateRefreshTokenCookie(String subject, String issuer) {
		int maxAge = 7 * 24 * 3600; // expires in 7 days
		String refreshToken = JWT.create()
								 .withSubject(subject)
								 .withExpiresAt(Date.from(Instant.now().plusSeconds(maxAge)))
								 .withIssuer(issuer)
								 .sign(algorithm);
		return createCookie(refreshToken, maxAge);
	}

	public static DecodedJWT verifyToken(String token) {
		JWTVerifier verifier = JWT.require(algorithm).build();
		return verifier.verify(token);
	}

	public static Cookie createCookie(String value, int maxAge) {
		Cookie cookie = new Cookie("API_refresh-token", value);
		cookie.setMaxAge(maxAge);
		cookie.setSecure(isProduction);
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		return cookie;
	}

}
