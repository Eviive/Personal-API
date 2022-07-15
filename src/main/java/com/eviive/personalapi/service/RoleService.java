package com.eviive.personalapi.service;

import com.eviive.personalapi.model.Role;
import com.eviive.personalapi.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleService implements BasicService<Role> {
	
	private final RoleRepository roleRepository;
	
	@Override
	public List<Role> findAll() {
		return roleRepository.findAll();
	}
	
	@Override
	public Optional<Role> findById(Long id) {
		return roleRepository.findById(id);
	}
	
	@Override
	public Role save(Role role) {
		return roleRepository.save(role);
	}
	
	@Override
	public void deleteById(Long id) {
		roleRepository.deleteById(id);
	}
	
}