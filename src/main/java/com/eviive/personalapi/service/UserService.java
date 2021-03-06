package com.eviive.personalapi.service;

import com.eviive.personalapi.model.ApiUser;
import com.eviive.personalapi.model.Role;
import com.eviive.personalapi.repository.RoleRepository;
import com.eviive.personalapi.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService extends AbstractService<ApiUser> implements UserDetailsService {
	
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	
	public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
		super(userRepository);
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<ApiUser> optUser = ((UserRepository)getRepository()).findByUsername(username);
		
		if (optUser.isEmpty()) {
			throw new UsernameNotFoundException("User " + username + " not found in the DB");
		}
		
		ApiUser user = optUser.get();
		
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		for (Role role: user.getRoles()) {
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		}
		
		return new User(user.getUsername(), user.getPassword(), authorities);
	}
	
	@Override
	public ApiUser save(@Valid ApiUser user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return getRepository().save(user);
	}
	
	public Optional<ApiUser> findByUsername(String username) {
		return ((UserRepository)getRepository()).findByUsername(username);
	}
	
	public void addRoleToUser(Long userId, Long... roleIds) {
		Optional<ApiUser> optUser = getRepository().findById(userId);
		
		if (optUser.isPresent()) {
			Optional<Role> optRole;
			for (Long roleId: roleIds) {
				optRole = roleRepository.findById(roleId);
				
				optRole.ifPresent(r -> optUser.get().addRole(r));
			}
;		}
	}
	
}