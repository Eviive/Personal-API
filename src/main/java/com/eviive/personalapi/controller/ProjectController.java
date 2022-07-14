package com.eviive.personalapi.controller;

import com.eviive.personalapi.model.Project;
import com.eviive.personalapi.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("projects")
@RequiredArgsConstructor
public class ProjectController implements BasicController<Project> {
	
	private final ProjectService projectService;
	
	@Override
	public ResponseEntity<List<Project>> findAll() {
		return ResponseEntity.ok().body(projectService.findAll());
	}
	
	@Override
	public ResponseEntity<Project> findById(Long id) {
		Optional<Project> optProject = projectService.findById(id);
		
		if (optProject.isPresent()) {
			return ResponseEntity.ok().body(optProject.get());
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@Override
	public ResponseEntity<Project> save(Project project, HttpServletRequest req) {
		URI uri = URI.create(req.getRequestURL().toString());
		return ResponseEntity.created(uri).body(projectService.save(project));
	}
	
	@Override
	public ResponseEntity<Project> update() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public ResponseEntity<Project> patch(Long id, Map<String, String> fields) {
		Optional<Project> optProject = projectService.findById(id);
		
		if (optProject.isPresent()) {
			fields.forEach((key, value) -> {
				Field field = ReflectionUtils.findField(Project.class, key);
				if (field != null) {
					field.setAccessible(true);
					ReflectionUtils.setField(field, optProject.get(), value);
				}
			});
			
			return ResponseEntity.ok().body(projectService.save(optProject.get()));
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@Override
	public ResponseEntity<Project> delete(Long id) {
		throw new UnsupportedOperationException();
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