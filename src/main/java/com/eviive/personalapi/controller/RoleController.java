package com.eviive.personalapi.controller;

import com.eviive.personalapi.dto.RoleDTO;
import com.eviive.personalapi.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("role")
@RequiredArgsConstructor
public class RoleController {

	private final RoleService roleService;

	// GET

    @GetMapping(path = "{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<RoleDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(roleService.findById(id));
    }

	@GetMapping(produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<List<RoleDTO>> findAll() {
		return ResponseEntity.ok().body(roleService.findAll());
	}

}
