package com.eviive.personalapi.filter;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.eviive.personalapi.util.JsonUtilities.generateErrorBody;
import static com.eviive.personalapi.util.TokenUtilities.verifyToken;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.WWW_AUTHENTICATE;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
public class AuthorizationFilter extends OncePerRequestFilter {

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

            DecodedJWT decodedToken = verifyToken(token);

            String username = decodedToken.getSubject();

            Claim claim = decodedToken.getClaim("roles");
            if (claim.isNull()) {
                throw new IllegalStateException("Roles are missing from the token.");
            }

            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            for (String role: claim.asArray(String.class)) {
                authorities.add(new SimpleGrantedAuthority(role));
            }

            Authentication authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            filterChain.doFilter(req, res);
        } catch (Exception e) {
            res.setStatus(UNAUTHORIZED.value());
            res.setContentType(APPLICATION_JSON_VALUE);
            res.setHeader(WWW_AUTHENTICATE, "Bearer realm=\"Personal-API\"");

            Map<String, Object> body = generateErrorBody(UNAUTHORIZED, e.getMessage());
            res.getWriter().write(new ObjectMapper().writeValueAsString(body));
        }
    }

}
