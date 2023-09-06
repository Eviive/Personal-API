package com.eviive.personalapi.controller;

import com.eviive.personalapi.dto.AuthRequestDTO;
import com.eviive.personalapi.dto.AuthResponseDTO;
import com.eviive.personalapi.dto.UserDTO;
import com.eviive.personalapi.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

    // GET

    @GetMapping(path = "{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(userService.findById(id));
    }

	@GetMapping(produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<List<UserDTO>> findAll() {
		return ResponseEntity.ok().body(userService.findAll());
	}

    // POST

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> save(@RequestBody @Valid UserDTO userDTO) {
        UserDTO createdUserDTO = userService.save(userDTO);

        URI uri = URI.create(String.format("/user/%s", createdUserDTO.getId()));

        return ResponseEntity.created(uri).body(createdUserDTO);
    }

    @PostMapping(path = "login", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid AuthRequestDTO loginForm, HttpServletRequest req, HttpServletResponse res) {
        AuthResponseDTO responseBody = userService.login(loginForm.getUsername(), loginForm.getPassword(), req, res);

        return ResponseEntity.ok().body(responseBody);
    }

    @PostMapping(path = "logout", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> logout(HttpServletResponse res) {
        userService.logout(res);

        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "refresh", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthResponseDTO> refreshToken(@CookieValue(value = "API_refresh-token", required = false) String refreshToken, HttpServletRequest req) {
        AuthResponseDTO responseBody = userService.refreshToken(refreshToken, req);

        return ResponseEntity.ok().body(responseBody);
    }

    // PUT

    @PutMapping(path = "{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> update(@PathVariable Long id, @RequestBody @Valid UserDTO userDTO) {
        return ResponseEntity.ok().body(userService.update(id, userDTO));
    }

    // DELETE

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);

        return ResponseEntity.noContent().build();
    }

}
