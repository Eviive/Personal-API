package com.eviive.personalapi.controller;

import com.eviive.personalapi.dto.AuthRequestDTO;
import com.eviive.personalapi.dto.AuthResponseDTO;
import com.eviive.personalapi.dto.CurrentUserDTO;
import com.eviive.personalapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.eviive.personalapi.util.TokenUtilities.REFRESH_TOKEN_COOKIE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
@Tag(name = "UserController")
public class UserController {

    private final UserService userService;

    // GET

    @GetMapping(path = "current", produces = APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Current user",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not Found")
        }
    )
    public ResponseEntity<CurrentUserDTO> current(@CurrentSecurityContext final SecurityContext securityContext) {
        return ResponseEntity.ok(userService.getCurrentUser(securityContext));
    }

    // POST

    @PostMapping(
        path = "login",
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Operation(
        summary = "Login",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
        }
    )
    public ResponseEntity<AuthResponseDTO> login(
        @RequestBody @Valid final AuthRequestDTO loginForm,
        final HttpServletRequest req,
        final HttpServletResponse res
    ) {
        return ResponseEntity.ok(
            userService.login(
                loginForm.username(),
                loginForm.password(),
                req,
                res
            )
        );
    }

    @PostMapping(path = "logout", produces = APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Logout",
        responses = @ApiResponse(responseCode = "204", description = "No Content")
    )
    public ResponseEntity<Void> logout(final HttpServletResponse res) {
        userService.logout(res);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "refresh", produces = APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Refresh token",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not Found")
        }
    )
    public ResponseEntity<AuthResponseDTO> refreshToken(
        @CookieValue(value = REFRESH_TOKEN_COOKIE, required = false) final String refreshToken
    ) {
        return ResponseEntity.ok(userService.refreshToken(refreshToken));
    }

}
