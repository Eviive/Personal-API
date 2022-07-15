package com.eviive.personalapi.service;

import com.eviive.personalapi.model.Role;
import com.eviive.personalapi.repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleService extends AbstractService<Role> {
	
	public RoleService(RoleRepository roleRepository) {
		super(roleRepository);
	}
	
}