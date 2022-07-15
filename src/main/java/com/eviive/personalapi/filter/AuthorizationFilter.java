package com.eviive.personalapi.filter;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.eviive.personalapi.util.TokenUtilities.verifyToken;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class AuthorizationFilter extends OncePerRequestFilter {
	
	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
		String authorizationHeader = req.getHeader(AUTHORIZATION);
		
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			try {
				String token = authorizationHeader.substring("Bearer ".length());
				
				DecodedJWT decodedToken = verifyToken(token);
				
				String username = decodedToken.getSubject();
				
				List<SimpleGrantedAuthority> authorities = new ArrayList<>();
				Claim claim = decodedToken.getClaim("roles");
				if (!claim.isNull() && !claim.isMissing()) {
					for (String role: claim.asArray(String.class)) {
						authorities.add(new SimpleGrantedAuthority(role));
					}
				}
				
				Authentication authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
				
				filterChain.doFilter(req, res);
			} catch (Exception e) {
				res.setStatus(FORBIDDEN.value());
				res.setContentType(APPLICATION_JSON_VALUE);
				
				Map<String, String> error = new HashMap<>();
				error.put("error_message", e.getMessage());
				
				res.getOutputStream().print(new ObjectMapper().writeValueAsString(error));
				res.flushBuffer();
			}
		} else {
			filterChain.doFilter(req, res);
		}
	}
	
}