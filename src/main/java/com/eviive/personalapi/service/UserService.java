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
import com.eviive.personalapi.util.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.eviive.personalapi.exception.PersonalApiErrorsEnum.*;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final TokenUtils tokenUtils;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final PasswordEncoder passwordEncoder;

    public UserDTO findById(Long id) {
        User user = userRepository.findById(id)
                                  .orElseThrow(() -> PersonalApiException.format(API404_USER_ID_NOT_FOUND, id));
        return userMapper.toDTO(user);
    }

    public UserDTO findByUsername(String username) {
        User user = userRepository.findByUsername(username)
                                  .orElseThrow(() -> PersonalApiException.format(API404_USERNAME_NOT_FOUND, username));
        return userMapper.toDTO(user);
    }

    public List<UserDTO> findAll() {
        return userMapper.toListDTO(userRepository.findAll());
    }

    public UserDTO save(UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userMapper.toDTO(userRepository.save(user));
    }

    public UserDTO update(Long id, UserDTO userDTO) {
        User originalUser = userRepository.findById(id)
                                          .orElseThrow(() -> PersonalApiException.format(API404_USER_ID_NOT_FOUND, id));

        User user = userMapper.toEntity(userDTO);

        user.setId(id);
        user.setPassword(originalUser.getPassword());

        return userMapper.toDTO(userRepository.save(user));
    }

    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw PersonalApiException.format(API404_USER_ID_NOT_FOUND, id);
        }

        userRepository.deleteById(id);
    }

    public AuthResponseDTO login(String username, String password, HttpServletRequest req, HttpServletResponse res) {
        try {
            Authentication authentication = authenticationConfiguration.getAuthenticationManager()
                                                                       .authenticate(new UsernamePasswordAuthenticationToken(username, password));

            org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
            String subject = user.getUsername();
            String issuer = req.getRequestURL().toString();
            List<String> claims = user.getAuthorities()
                                      .stream()
                                      .map(GrantedAuthority::getAuthority)
                                      .toList();

            AuthResponseDTO responseBody = new AuthResponseDTO();
            responseBody.setUsername(subject);
            responseBody.setRoles(claims);
            responseBody.setAccessToken(tokenUtils.generateAccessToken(subject, issuer, claims));

            res.addCookie(tokenUtils.generateRefreshTokenCookie(subject, issuer));

            return responseBody;
        } catch (AuthenticationException e) {
            throw PersonalApiException.format(e, API401_LOGIN_ERROR, e.getMessage());
        } catch (Exception e) {
            throw PersonalApiException.format(e, API500_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public void logout(HttpServletResponse res) {
        res.addCookie(tokenUtils.createCookie(null, 0));
    }

    public AuthResponseDTO refreshToken(String refreshToken, HttpServletRequest req) {
        if (refreshToken == null) {
            throw PersonalApiException.format(API401_TOKEN_ERROR, "Refresh token not found");
        }

        try {
            DecodedJWT decodedToken = tokenUtils.verifyToken(refreshToken);

            UserDTO user = findByUsername(decodedToken.getSubject());

            List<String> claims = user.getRoles()
                                      .stream()
                                      .map(RoleDTO::getName)
                                      .toList();

            String accessToken = tokenUtils.generateAccessToken(user.getUsername(), req.getRequestURL().toString(), claims);

            AuthResponseDTO responseBody = new AuthResponseDTO();
            responseBody.setUsername(user.getUsername());
            responseBody.setRoles(claims);
            responseBody.setAccessToken(accessToken);
            return responseBody;
        } catch (JWTVerificationException e) {
            throw PersonalApiException.format(e, API401_TOKEN_ERROR, e.getMessage());
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                                  .orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found", username)));

        List<SimpleGrantedAuthority> authorities = user.getRoles()
                                                       .stream()
                                                       .map(r -> new SimpleGrantedAuthority(r.getName().toString()))
                                                       .toList();

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

}
