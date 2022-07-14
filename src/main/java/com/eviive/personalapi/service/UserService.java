package com.eviive.personalapi.service;

import com.eviive.personalapi.model.ApiUser;
import com.eviive.personalapi.model.Role;
import com.eviive.personalapi.repository.RoleRepository;
import com.eviive.personalapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
	
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<ApiUser> optApiUser = userRepository.findByUsername(username);
		
		if (optApiUser.isEmpty()) {
			throw new UsernameNotFoundException("User " + username + " not found in the DB");
		}
		
		ApiUser apiUser = optApiUser.get();
		
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		for (Role role: apiUser.getRoles()) {
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		}
		
		return new User(apiUser.getUsername(), apiUser.getPassword(), authorities);
	}
	
	public Optional<ApiUser> findByUsername(String username) {
		return userRepository.findByUsername(username);
	}
	
	public ApiUser saveUser(ApiUser apiUser) {
		apiUser.setPassword(passwordEncoder.encode(apiUser.getPassword()));
		return userRepository.save(apiUser);
	}
	
	public Role saveRole(Role role) {
		return roleRepository.save(role);
	}
	
	public void addRoleToUser(String username, String roleName) {
		Optional<ApiUser> optApiUser = userRepository.findByUsername(username);
		Optional<Role> optRole = roleRepository.findByName(roleName);
		
		if (optApiUser.isPresent() && optRole.isPresent()) {
			optApiUser.get().addRole(optRole.get())
;		}
	}
	
}