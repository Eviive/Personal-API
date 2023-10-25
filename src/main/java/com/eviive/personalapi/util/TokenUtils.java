package com.eviive.personalapi.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Component
public final class TokenUtils {

    private final Algorithm algorithm;
    private final JWTVerifier verifier;

    @Value("${is-production}")
    private boolean isProduction;

    public TokenUtils(@Value("${jwt-secret-key}") String secret) {
        this.algorithm = Algorithm.HMAC256(secret);
        this.verifier = JWT.require(algorithm).build();
    }

    public String generateAccessToken(String subject, String issuer, List<String> claims) {
        int maxAge = 15 * 60; // expires in 15 minutes
        return JWT.create()
                  .withSubject(subject)
                  .withExpiresAt(Date.from(Instant.now().plusSeconds(maxAge)))
                  .withIssuer(issuer)
                  .withClaim("roles", claims)
                  .sign(algorithm);
    }

    public Cookie generateRefreshTokenCookie(String subject, String issuer) {
        int maxAge = 7 * 24 * 3600; // expires in 7 days
        String refreshToken = JWT.create()
                                 .withSubject(subject)
                                 .withExpiresAt(Date.from(Instant.now().plusSeconds(maxAge)))
                                 .withIssuer(issuer)
                                 .sign(algorithm);
        return createCookie(refreshToken, maxAge);
    }

    public DecodedJWT verifyToken(String token) {
        return verifier.verify(token);
    }

    public Cookie createCookie(String value, int maxAge) {
        Cookie cookie = new Cookie("API_refresh-token", value);
        cookie.setMaxAge(maxAge);
        cookie.setSecure(isProduction);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

}
