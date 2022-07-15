package com.eviive.personalapi.controller;

import com.eviive.personalapi.mapper.ModelMapper;
import com.eviive.personalapi.model.Project;
import com.eviive.personalapi.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("projects")
public class ProjectController extends AbstractController<Project> {
	
	public ProjectController(ProjectService service) {
		super(service, new ModelMapper<>(Project.class));
	}
	
	@GetMapping(
			path = "featured",
			produces = APPLICATION_JSON_VALUE
	)
	public ResponseEntity<List<Project>> findAllFeatured() {
		List<Project> results = ((ProjectService)getService()).findAllFeatured();
		
		return ResponseEntity.ok().body(results);
	}
	
	@GetMapping(
			path = "not-featured",
			produces = APPLICATION_JSON_VALUE
	)
	public ResponseEntity<List<Project>> findAllNotFeatured() {
		List<Project> results = ((ProjectService)getService()).findAllNotFeatured();
		
		return ResponseEntity.ok().body(results);
	}
	
}