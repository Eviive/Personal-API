package com.eviive.personalapi.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.eviive.personalapi.form.RefreshTokenForm;
import com.eviive.personalapi.model.ApiUser;
import com.eviive.personalapi.model.Role;
import com.eviive.personalapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.eviive.personalapi.utils.TokenUtilities.generateAccessToken;
import static com.eviive.personalapi.utils.TokenUtilities.verifyToken;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController implements BasicController<ApiUser> {
	
	private final UserService userService;
	
	@Override
	public ResponseEntity<List<ApiUser>> findAll() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public ResponseEntity<ApiUser> findById(Long id) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public ResponseEntity<ApiUser> save(ApiUser element, HttpServletRequest req) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public ResponseEntity<ApiUser> update() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public ResponseEntity<ApiUser> patch(Long id, Map<String, String> fields) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public ResponseEntity<ApiUser> delete(Long id) {
		throw new UnsupportedOperationException();
	}
	
	@PostMapping(
			path = "refresh-token",
			consumes = APPLICATION_JSON_VALUE,
			produces = APPLICATION_JSON_VALUE
	)
	public ResponseEntity<Map<String, String>> refreshToken(@RequestBody RefreshTokenForm form, HttpServletRequest req) {
		String refreshToken = form.getRefreshToken();
		Map<String, String> body = new HashMap<>();
		
		if (refreshToken != null) {
			try {
				DecodedJWT decodedToken = verifyToken(refreshToken);
				
				Optional<ApiUser> optUser = userService.findByUsername(decodedToken.getSubject());
				
				if (optUser.isEmpty()) {
					throw new IllegalArgumentException("The token's subject isn't valid");
				}
				
				ApiUser user = optUser.get();
				
				List<String> claims = user.getRoles().stream()
													 .map(Role::getName)
													 .collect(Collectors.toList());
				
				String accessToken = generateAccessToken(user.getUsername(), req.getRequestURL().toString(), claims);
				
				body.put("access_token", accessToken);
				return ResponseEntity.ok().body(body);
			} catch (Exception e) {
				body.put("error_message", e.getMessage());
			}
		} else {
			body.put("error_message", "Refresh token is missing");
		}
		
		return ResponseEntity.badRequest().body(body);
	}
	
}