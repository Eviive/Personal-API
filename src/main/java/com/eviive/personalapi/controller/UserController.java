package com.eviive.personalapi.controller;

import com.eviive.personalapi.dto.AuthRequestDTO;
import com.eviive.personalapi.dto.AuthResponseDTO;
import com.eviive.personalapi.dto.UserDTO;
import com.eviive.personalapi.service.UserService;
import com.eviive.personalapi.util.UriUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final UriUtils uriUtils;

    // GET

    @GetMapping(path = "{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> findById(@PathVariable final Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDTO>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    // POST

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> save(@RequestBody @Valid final UserDTO userDTO) {
        final UserDTO createdUser = userService.save(userDTO);
        final URI location = uriUtils.buildLocation(createdUser.getId());
        return ResponseEntity.created(location)
            .body(createdUser);
    }

    @PostMapping(
        path = "login",
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<AuthResponseDTO> login(
        @RequestBody @Valid final AuthRequestDTO loginForm,
        final HttpServletRequest req,
        final HttpServletResponse res
    ) {
        return ResponseEntity.ok(
            userService.login(
                loginForm.getUsername(),
                loginForm.getPassword(),
                req,
                res
            )
        );
    }

    @PostMapping(path = "logout", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> logout(final HttpServletResponse res) {
        userService.logout(res);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "refresh", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthResponseDTO> refreshToken(
        @CookieValue(value = "API_refresh-token", required = false) final String refreshToken,
        final HttpServletRequest req
    ) {
        return ResponseEntity.ok(userService.refreshToken(refreshToken, req));
    }

    // PUT

    @PutMapping(path = "{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> update(
        @PathVariable final Long id,
        @RequestBody @Valid final UserDTO userDTO
    ) {
        return ResponseEntity.ok(userService.update(id, userDTO));
    }

    // DELETE

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
