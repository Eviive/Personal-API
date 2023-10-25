package com.eviive.personalapi.filter;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.eviive.personalapi.util.TokenUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
public class AuthorizationFilter extends OncePerRequestFilter {

    private final TokenUtils tokenUtils;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest req, @NonNull HttpServletResponse res, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = req.getHeader(AUTHORIZATION);

        if (authorizationHeader == null) {
            filterChain.doFilter(req, res);
            return;
        }

        try {
            if (!authorizationHeader.startsWith("Bearer ")) {
                throw new IllegalStateException("The access token must be a bearer token.");
            }

            String token = authorizationHeader.substring("Bearer ".length());

            DecodedJWT decodedToken = tokenUtils.verifyToken(token);

            String username = decodedToken.getSubject();

            Claim claim = decodedToken.getClaim("roles");

            if (claim.isNull() || claim.isMissing()) {
                throw new IllegalStateException("Roles are missing from the token.");
            }

            List<SimpleGrantedAuthority> authorities = claim.asList(String.class)
                                                            .stream()
                                                            .map(SimpleGrantedAuthority::new)
                                                            .toList();

            Authentication authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } catch (Exception e) {
            if (logger.isDebugEnabled()) {
                logger.debug("Authentication failed: %s".formatted(e.getMessage()), e);
            }
        } finally {
            filterChain.doFilter(req, res);
        }
    }

}
