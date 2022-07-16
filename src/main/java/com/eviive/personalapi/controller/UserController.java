package com.eviive.personalapi.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.eviive.personalapi.mapper.ModelMapper;
import com.eviive.personalapi.model.ApiUser;
import com.eviive.personalapi.model.Role;
import com.eviive.personalapi.service.AbstractService;
import com.eviive.personalapi.service.UserService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.eviive.personalapi.util.TokenUtilities.generateAccessToken;
import static com.eviive.personalapi.util.TokenUtilities.verifyToken;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("users")
public class UserController extends AbstractController<ApiUser> {
	
	private final AbstractService<Role> roleService;
	
	public UserController(AbstractService<ApiUser> service, AbstractService<Role> roleService) {
		super(service, new ModelMapper<>(ApiUser.class));
		this.roleService = roleService;
	}
	
	@Override
	protected boolean isElementInvalid(ApiUser user) {
		for (Role role: user.getRoles()) {
			if (!roleService.existsById(role.getId())) {
				return true;
			}
		}
		return false;
	}
	
	@PostMapping(
			path = "refresh-token",
			consumes = APPLICATION_JSON_VALUE,
			produces = APPLICATION_JSON_VALUE
	)
	public ResponseEntity<Map<String, String>> refreshToken(@Valid @RequestBody RefreshTokenForm form, HttpServletRequest req) {
		String refreshToken = form.refreshToken;
		Map<String, String> body = new HashMap<>();
		
		if (refreshToken != null) {
			try {
				DecodedJWT decodedToken = verifyToken(refreshToken);
				
				Optional<ApiUser> optUser = ((UserService)getService()).findByUsername(decodedToken.getSubject());
				
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
	
	@Data
	private static class RefreshTokenForm {
		
		@NotBlank
		private String refreshToken;
		
	}
	
}