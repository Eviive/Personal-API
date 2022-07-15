package com.eviive.personalapi.controller;

import com.eviive.personalapi.mapper.RoleMapper;
import com.eviive.personalapi.model.Role;
import com.eviive.personalapi.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("roles")
@RequiredArgsConstructor
public class RoleController implements BasicController<Role> {
	
	private final RoleService roleService;
	private final RoleMapper mapper;
	
	@Override
	public ResponseEntity<List<Role>> findAll() {
		return ResponseEntity.ok().body(roleService.findAll());
	}
	
	@Override
	public ResponseEntity<Role> findById(Long id) {
		Optional<Role> optRole = roleService.findById(id);
		
		if (optRole.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok().body(optRole.get());
	}
	
	@Override
	public ResponseEntity<Role> save(Role role, HttpServletRequest req) {
		Role createdRole = roleService.save(role);
		URI uri = URI.create(req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/projects/" + createdRole.getId());
		
		return ResponseEntity.created(uri).body(createdRole);
	}
	
	@Override
	public ResponseEntity<Role> update() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public ResponseEntity<Role> patch(Long id, Role role) {
		Optional<Role> optRole = roleService.findById(id);
		
		if (optRole.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		mapper.map(role, optRole.get());
		
		return ResponseEntity.ok().body(roleService.save(optRole.get()));
	}
	
	@Override
	public ResponseEntity<String> delete(Long id) {
		if (roleService.findById(id).isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		roleService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
	
}