package com.eviive.personalapi.controller;

import com.eviive.personalapi.mapper.ModelMapper;
import com.eviive.personalapi.model.Project;
import com.eviive.personalapi.model.Skill;
import com.eviive.personalapi.service.AbstractService;
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
	
	private final AbstractService<Skill> skillService;
	
	public ProjectController(AbstractService<Project> service, AbstractService<Skill> skillService) {
		super(service, new ModelMapper<>(Project.class));
		this.skillService = skillService;
	}
	
	@Override
	protected boolean isElementInvalid(Project project) {
		for (Skill skill: project.getSkills()) {
			if (!skillService.existsById(skill.getId())) {
				return true;
			}
		}
		return false;
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