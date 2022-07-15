package com.eviive.personalapi.controller;

import com.eviive.personalapi.mapper.ModelMapper;
import com.eviive.personalapi.model.Role;
import com.eviive.personalapi.service.AbstractService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("roles")
public class RoleController extends AbstractController<Role> {
	
	public RoleController(AbstractService<Role> service) {
		super(service, new ModelMapper<>(Role.class));
	}
	
}