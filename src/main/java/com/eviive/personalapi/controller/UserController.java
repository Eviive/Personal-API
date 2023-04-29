package com.eviive.personalapi.controller;

import com.eviive.personalapi.dto.UserDTO;
import com.eviive.personalapi.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

    @GetMapping(path = "{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(userService.findById(id));
    }

	@GetMapping(produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<List<UserDTO>> findAll() {
		return ResponseEntity.ok().body(userService.findAll());
	}

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> save(@RequestBody @Valid UserDTO userDTO) {
        UserDTO createdUserDTO = userService.save(userDTO);

        URI uri = URI.create(String.format("/user/%s", createdUserDTO.getId()));

        return ResponseEntity.created(uri).body(createdUserDTO);
    }

    @PutMapping(path = "{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> update(@PathVariable("id") Long id, @RequestBody @Valid UserDTO userDTO) {
        return ResponseEntity.ok().body(userService.update(id, userDTO));
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        userService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "login", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginForm form, HttpServletRequest req, HttpServletResponse res) {
        Map<String, Object> body = userService.login(form.getUsername(), form.getPassword(), req, res);

        return ResponseEntity.ok().body(body);
    }

    @PostMapping(path = "logout", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> logout(HttpServletResponse res) {
        userService.logout(res);

        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "refresh", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> refreshToken(@CookieValue("API_refresh-token") String refreshToken, HttpServletRequest req) {
        Map<String, Object> body = userService.refreshToken(refreshToken, req);

        return ResponseEntity.ok().body(body);
    }

    @Data
    private static class LoginForm {

        @NotBlank(message = "The username is required.")
        private String username;

        @NotBlank(message = "The password is required.")
        private String password;

    }

}
