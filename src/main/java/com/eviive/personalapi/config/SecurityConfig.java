package com.eviive.personalapi.config;

import com.eviive.personalapi.exception.PersonalApiExceptionHandler;
import com.eviive.personalapi.filter.AuthorizationFilter;
import com.eviive.personalapi.properties.CorsPropertiesConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static com.eviive.personalapi.entity.RoleEnum.ROLE_ADMIN;
import static com.eviive.personalapi.entity.RoleEnum.ROLE_USER;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.ORIGIN;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthorizationFilter authorizationFilter;

    private final PersonalApiExceptionHandler personalApiExceptionHandler;

    @Bean
    @SuppressWarnings({"checkstyle:LambdaBodyLength", "checkstyle:MultipleStringLiterals"})
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        return http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(Customizer.withDefaults())
            .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
            .addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests(auth ->
                auth.requestMatchers(
                        "/v2/api-docs",
                        "/v3/api-docs",
                        "/v3/api-docs/**",
                        "/swagger-resources",
                        "/swagger-resources/**",
                        "/configuration/ui",
                        "/configuration/security",
                        "/swagger-ui/**",
                        "/webjars/**",
                        "/swagger-ui.html"
                    )
                    .permitAll()


                    .requestMatchers(POST, "/user/login", "/user/logout", "/user/refresh")
                    .permitAll()

                    .requestMatchers("/user/**")
                    .hasAuthority(ROLE_ADMIN.toString())

                    .requestMatchers("/role/**")
                    .hasAuthority(ROLE_ADMIN.toString())

                    .requestMatchers(GET, "/project/**")
                    .permitAll()
                    .requestMatchers("/project/**")
                    .hasAuthority(ROLE_USER.toString())

                    .requestMatchers(GET, "/skill/**")
                    .permitAll()
                    .requestMatchers("/skill/**")
                    .hasAuthority(ROLE_USER.toString())

                    .requestMatchers(GET, "/image/**")
                    .permitAll()
                    .requestMatchers("/image/**")
                    .hasAuthority(ROLE_USER.toString())

                    .requestMatchers("/actuator/**")
                    .hasAuthority(ROLE_ADMIN.toString())

                    // deny-by-default policy
                    .anyRequest()
                    .denyAll()
            )
            .exceptionHandling(exceptionHandling ->
                exceptionHandling
                    .authenticationEntryPoint(personalApiExceptionHandler)
                    .accessDeniedHandler(personalApiExceptionHandler)
            )
            .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(
        final CorsPropertiesConfig corsPropertiesConfig
    ) {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(corsPropertiesConfig.allowedOrigins());
        configuration.addAllowedMethod("*");
        configuration.setAllowedHeaders(List.of(AUTHORIZATION, ORIGIN, CONTENT_TYPE));
        configuration.setAllowCredentials(true);
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
