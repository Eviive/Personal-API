package com.eviive.personalapi.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthorizationFilter authorizationFilter;

    @Value("${allowed-origins}")
    private List<String> allowedOrigins;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.cors()
                   .and()
                   .csrf().disable()
                   .sessionManagement().sessionCreationPolicy(STATELESS)
                   .and()
                   .addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class)
                   .authorizeHttpRequests()
                       .requestMatchers(POST, "/users/login", "/users/logout", "/users/refresh").permitAll()
                       .requestMatchers("/users/**").hasAuthority("ROLE_ADMIN")

                       .requestMatchers("/roles/**").hasAuthority("ROLE_ADMIN")

                       .requestMatchers(GET, "/projects/**").permitAll()
                       .requestMatchers("/projects/**").hasAuthority("ROLE_USER")

                       .requestMatchers(GET, "/skills/**").permitAll()
                       .requestMatchers("/skills/**").hasAuthority("ROLE_USER")

                       .requestMatchers(GET, "/actuator/**").hasAuthority("ROLE_ADMIN")
                       .requestMatchers(POST, "/actuator/shutdown").hasAuthority("ROLE_ADMIN")

                       .requestMatchers(GET, "/documentation").permitAll()

                       .anyRequest().denyAll() // deny-by-default policy
                   .and()
                   .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(allowedOrigins);
        configuration.addAllowedMethod("*");
        configuration.setAllowedHeaders(List.of(AUTHORIZATION, ORIGIN, CONTENT_TYPE));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
