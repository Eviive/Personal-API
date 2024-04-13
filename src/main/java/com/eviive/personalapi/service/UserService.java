package com.eviive.personalapi.service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.eviive.personalapi.dto.AuthResponseDTO;
import com.eviive.personalapi.entity.User;
import com.eviive.personalapi.exception.PersonalApiException;
import com.eviive.personalapi.repository.UserRepository;
import com.eviive.personalapi.util.TokenUtilities;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.eviive.personalapi.exception.PersonalApiErrorsEnum.API400_REFRESH_TOKEN_NOT_FOUND;
import static com.eviive.personalapi.exception.PersonalApiErrorsEnum.API401_LOGIN_FAILED;
import static com.eviive.personalapi.exception.PersonalApiErrorsEnum.API401_TOKEN_ERROR;
import static com.eviive.personalapi.exception.PersonalApiErrorsEnum.API404_USERNAME_NOT_FOUND;
import static com.eviive.personalapi.exception.PersonalApiErrorsEnum.API500_INTERNAL_SERVER_ERROR;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final TokenUtilities tokenUtilities;

    private final AuthenticationConfiguration authenticationConfiguration;

    @SuppressWarnings("checkstyle:IllegalCatch")
    public AuthResponseDTO login(
        final String username,
        final String password,
        final HttpServletResponse res
    ) {
        try {
            final Authentication authentication = authenticationConfiguration
                .getAuthenticationManager()
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));

            final User user = (User) authentication.getPrincipal();

            final String accessToken = tokenUtilities.generateAccessToken(user);

            res.addCookie(tokenUtilities.generateRefreshTokenCookie(user));

            return new AuthResponseDTO(accessToken);
        } catch (AuthenticationException e) {
            throw PersonalApiException.format(e, API401_LOGIN_FAILED, e.getMessage());
        } catch (Exception e) {
            throw PersonalApiException.format(e, API500_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public void logout(final HttpServletResponse res) {
        res.addCookie(tokenUtilities.deleteCookie());
    }

    public AuthResponseDTO refreshToken(final String refreshToken) {
        if (refreshToken == null) {
            throw new PersonalApiException(API400_REFRESH_TOKEN_NOT_FOUND);
        }

        try {
            final DecodedJWT decodedToken = tokenUtilities.verifyToken(refreshToken);

            final User user = userRepository.findByUsername(decodedToken.getSubject())
                .orElseThrow(() -> PersonalApiException.format(
                    API404_USERNAME_NOT_FOUND,
                    decodedToken.getSubject()
                ));

            final String accessToken = tokenUtilities.generateAccessToken(user);

            return new AuthResponseDTO(accessToken);
        } catch (JWTVerificationException e) {
            throw PersonalApiException.format(e, API401_TOKEN_ERROR, e.getMessage());
        }
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
            .orElseThrow(() ->
                new UsernameNotFoundException("User %s not found".formatted(username))
            );
    }

}
