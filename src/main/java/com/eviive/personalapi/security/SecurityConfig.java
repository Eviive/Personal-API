package com.eviive.personalapi.security;

import com.eviive.personalapi.filter.AuthenticationFilter;
import com.eviive.personalapi.filter.AuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final UserDetailsService userDetailsService;
	private final PasswordEncoder passwordEncoder;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
		
		AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
		AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager);
		authenticationFilter.setFilterProcessesUrl("/users/login");
		
		http.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(STATELESS);
		
		http.addFilter(authenticationFilter).authenticationManager(authenticationManager);
		http.addFilterAfter(new AuthorizationFilter(), AuthenticationFilter.class);
		
		http.authorizeRequests().antMatchers(POST, "/users/login", "/users/refresh-token").permitAll();
		http.authorizeRequests().antMatchers("/users/**").hasAnyAuthority("ROLE_ADMIN");
		
		http.authorizeRequests().antMatchers("/roles/**").hasAnyAuthority("ROLE_ADMIN");
		
		http.authorizeRequests().antMatchers(GET, "/projects/**").permitAll();
		http.authorizeRequests().antMatchers("/projects/**").hasAuthority("ROLE_ADMIN");
		
		http.authorizeRequests().antMatchers(GET, "/skills/**").permitAll();
		http.authorizeRequests().antMatchers("/skills/**").hasAuthority("ROLE_ADMIN");
		
		http.authorizeRequests().anyRequest().denyAll(); // deny-by-default policy
		
		return http.build();
	}
	
}