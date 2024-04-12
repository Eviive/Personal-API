package com.eviive.personalapi.service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.eviive.personalapi.dto.AuthResponseDTO;
import com.eviive.personalapi.dto.RoleDTO;
import com.eviive.personalapi.dto.UserDTO;
import com.eviive.personalapi.entity.User;
import com.eviive.personalapi.exception.PersonalApiException;
import com.eviive.personalapi.mapper.UserMapper;
import com.eviive.personalapi.repository.UserRepository;
import com.eviive.personalapi.util.TokenUtilities;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.eviive.personalapi.exception.PersonalApiErrorsEnum.API401_LOGIN_ERROR;
import static com.eviive.personalapi.exception.PersonalApiErrorsEnum.API401_TOKEN_ERROR;
import static com.eviive.personalapi.exception.PersonalApiErrorsEnum.API404_USERNAME_NOT_FOUND;
import static com.eviive.personalapi.exception.PersonalApiErrorsEnum.API500_INTERNAL_SERVER_ERROR;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final TokenUtilities tokenUtilities;

    private final AuthenticationConfiguration authenticationConfiguration;

    public UserDTO findByUsername(final String username) {
        final User user = userRepository.findByUsername(username)
            .orElseThrow(() -> PersonalApiException.format(API404_USERNAME_NOT_FOUND, username));
        return userMapper.toDTO(user);
    }

    @SuppressWarnings("checkstyle:IllegalCatch")
    public AuthResponseDTO login(
        final String username,
        final String password,
        final HttpServletResponse res
    ) {
        try {
            final Authentication authentication =
                authenticationConfiguration.getAuthenticationManager()
                    .authenticate(new UsernamePasswordAuthenticationToken(username, password));

            final org.springframework.security.core.userdetails.User user =
                (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

            final String subject = user.getUsername();
            final List<String> claims = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

            final AuthResponseDTO responseBody = new AuthResponseDTO();
            responseBody.setUsername(subject);
            responseBody.setRoles(claims);
            responseBody.setAccessToken(tokenUtilities.generateAccessToken(subject, claims));

            res.addCookie(tokenUtilities.generateRefreshTokenCookie(subject));

            return responseBody;
        } catch (AuthenticationException e) {
            throw PersonalApiException.format(e, API401_LOGIN_ERROR, e.getMessage());
        } catch (Exception e) {
            throw PersonalApiException.format(e, API500_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public void logout(final HttpServletResponse res) {
        res.addCookie(tokenUtilities.deleteCookie());
    }

    public AuthResponseDTO refreshToken(final String refreshToken) {
        if (refreshToken == null) {
            throw PersonalApiException.format(API401_TOKEN_ERROR, "Refresh token not found");
        }

        try {
            final DecodedJWT decodedToken = tokenUtilities.verifyToken(refreshToken);

            final UserDTO user = findByUsername(decodedToken.getSubject());

            final List<String> claims = user.getRoles()
                .stream()
                .map(RoleDTO::getName)
                .toList();

            final String accessToken =
                tokenUtilities.generateAccessToken(user.getUsername(), claims);

            final AuthResponseDTO responseBody = new AuthResponseDTO();
            responseBody.setUsername(user.getUsername());
            responseBody.setRoles(claims);
            responseBody.setAccessToken(accessToken);
            return responseBody;
        } catch (JWTVerificationException e) {
            throw PersonalApiException.format(e, API401_TOKEN_ERROR, e.getMessage());
        }
    }

    @Override
    public UserDetails loadUserByUsername(final String username) {
        final User user = userRepository.findByUsername(username)
            .orElseThrow(
                () -> new UsernameNotFoundException(String.format("User %s not found", username)));

        final List<SimpleGrantedAuthority> authorities = user.getRoles()
            .stream()
            .map(r -> new SimpleGrantedAuthority(r.getName().toString()))
            .toList();

        return new org.springframework.security.core.userdetails.User(user.getUsername(),
            user.getPassword(), authorities
        );
    }

}
