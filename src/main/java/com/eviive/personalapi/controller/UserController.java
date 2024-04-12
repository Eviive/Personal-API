package com.eviive.personalapi.controller;

import com.eviive.personalapi.dto.AuthRequestDTO;
import com.eviive.personalapi.dto.AuthResponseDTO;
import com.eviive.personalapi.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // POST

    @PostMapping(
        path = "login",
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<AuthResponseDTO> login(
        @RequestBody @Valid final AuthRequestDTO loginForm,
        final HttpServletResponse res
    ) {
        return ResponseEntity.ok(
            userService.login(
                loginForm.getUsername(),
                loginForm.getPassword(),
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
        @CookieValue(value = "API_refresh-token", required = false) final String refreshToken
    ) {
        return ResponseEntity.ok(userService.refreshToken(refreshToken));
    }

}
