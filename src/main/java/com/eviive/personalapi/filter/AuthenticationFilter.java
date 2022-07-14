package com.eviive.personalapi.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.eviive.personalapi.utils.TokenUtilities.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	private final AuthenticationManager authenticationManager;
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
		return authenticationManager.authenticate(authenticationToken);
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain, Authentication authentication) throws IOException {
		User user = (User)authentication.getPrincipal();
		String subject = user.getUsername();
		String issuer = req.getRequestURL().toString();
		List<String> claims = user.getAuthorities().stream()
												   .map(GrantedAuthority::getAuthority)
												   .collect(Collectors.toList());
		
		Map<String, String> tokens = new HashMap<>();
		tokens.put("access_token", generateAccessToken(subject, issuer, claims));
		tokens.put("refresh_token", generateRefreshToken(subject, issuer));
		
		res.setContentType(APPLICATION_JSON_VALUE);
		res.getOutputStream().print(new ObjectMapper().writeValueAsString(tokens));
		res.flushBuffer();
	}
	
}