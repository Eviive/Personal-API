package com.eviive.personalapi.controller;

import com.eviive.personalapi.mapper.ProjectMapper;
import com.eviive.personalapi.model.Project;
import com.eviive.personalapi.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("projects")
@RequiredArgsConstructor
public class ProjectController implements BasicController<Project> {
	
	private final ProjectService projectService;
	private final ProjectMapper mapper;
	
	@Override
	public ResponseEntity<List<Project>> findAll() {
		return ResponseEntity.ok().body(projectService.findAll());
	}
	
	@Override
	public ResponseEntity<Project> findById(Long id) {
		Optional<Project> optProject = projectService.findById(id);
		
		if (optProject.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok().body(optProject.get());
	}
	
	@Override
	public ResponseEntity<Project> save(Project project, HttpServletRequest req) {
		Project createdProject = projectService.save(project);
		URI uri = URI.create(req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/projects/" + createdProject.getId());
		
		return ResponseEntity.created(uri).body(createdProject);
	}
	
	@Override
	public ResponseEntity<Project> update() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public ResponseEntity<Project> patch(Long id, Project project) {
		Optional<Project> optProject = projectService.findById(id);
		
		if (optProject.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		mapper.map(project, optProject.get());
		
		return ResponseEntity.ok().body(projectService.save(optProject.get()));
	}
	
	@Override
	public ResponseEntity<String> delete(Long id) {
		if (projectService.findById(id).isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		projectService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping(
			path = "featured",
			produces = APPLICATION_JSON_VALUE
	)
	public ResponseEntity<List<Project>> findAllFeatured() {
		return ResponseEntity.ok().body(projectService.findAllFeatured());
	}
	
	@GetMapping(
			path = "not-featured",
			produces = APPLICATION_JSON_VALUE
	)
	public ResponseEntity<List<Project>> findAllNotFeatured() {
		return ResponseEntity.ok().body(projectService.findAllNotFeatured());
	}
	
}