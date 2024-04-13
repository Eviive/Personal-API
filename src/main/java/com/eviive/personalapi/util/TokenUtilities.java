package com.eviive.personalapi.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.eviive.personalapi.entity.User;
import com.eviive.personalapi.properties.JwtPropertiesConfig;
import com.eviive.personalapi.properties.PersonalApiPropertiesConfig;
import jakarta.servlet.http.Cookie;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public final class TokenUtilities {

    public static final String REFRESH_TOKEN_COOKIE_NAME = "personal-api_refresh-token";

    private final Algorithm algorithm;

    private final JWTVerifier verifier;

    private final UriUtilities uriUtilities;

    private final PersonalApiPropertiesConfig personalApiPropertiesConfig;

    private final JwtPropertiesConfig jwtPropertiesConfig;

    public TokenUtilities(
        final UriUtilities uriUtilities,
        final PersonalApiPropertiesConfig personalApiPropertiesConfig,
        final JwtPropertiesConfig jwtPropertiesConfig
    ) {
        this.algorithm = Algorithm.HMAC256(jwtPropertiesConfig.secret());
        this.verifier = JWT.require(algorithm).build();
        this.uriUtilities = uriUtilities;
        this.personalApiPropertiesConfig = personalApiPropertiesConfig;
        this.jwtPropertiesConfig = jwtPropertiesConfig;
    }

    public String generateAccessToken(final User user) {
        final List<String> authorities = user.getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .toList();

        return JWT.create()
            .withSubject(user.getUsername())
            .withExpiresAt(
                Instant
                    .now()
                    .plusSeconds(jwtPropertiesConfig.token().access().expiration())
            )
            .withIssuer(uriUtilities.getCurrentUri().toString())
            .withClaim("authorities", authorities)
            .sign(algorithm);
    }


    public Cookie generateRefreshTokenCookie(final User user) {
        final String refreshToken = JWT.create()
            .withSubject(user.getUsername())
            .withExpiresAt(
                Instant
                    .now()
                    .plusSeconds(jwtPropertiesConfig.token().refresh().expiration())
            )
            .withIssuer(uriUtilities.getCurrentUri().toString())
            .sign(algorithm);

        return createCookie(refreshToken, jwtPropertiesConfig.token().refresh().expiration());
    }

    public DecodedJWT verifyToken(final String token) {
        return verifier.verify(token);
    }

    public Cookie createCookie(final String value, final int maxAge) {
        final Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, value);
        cookie.setMaxAge(maxAge);
        cookie.setSecure(personalApiPropertiesConfig.production());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    public Cookie deleteCookie() {
        return createCookie(null, 0);
    }

}
