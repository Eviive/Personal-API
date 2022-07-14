package com.eviive.personalapi.controller;

import com.eviive.personalapi.model.Skill;
import com.eviive.personalapi.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("skills")
@RequiredArgsConstructor
public class SkillController implements BasicController<Skill> {
	
	private final SkillService skillService;
	
	@Override
	public ResponseEntity<List<Skill>> findAll() {
		return ResponseEntity.ok().body(skillService.findAll());
	}
	
	@Override
	public ResponseEntity<Skill> findById(Long id) {
		Optional<Skill> optSkill = skillService.findById(id);
		
		if (optSkill.isPresent()) {
			return ResponseEntity.ok().body(optSkill.get());
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@Override
	public ResponseEntity<Skill> save(Skill skill, HttpServletRequest req) {
		URI uri = URI.create(req.getRequestURL().toString());
		return ResponseEntity.created(uri).body(skillService.save(skill));
	}
	
	@Override
	public ResponseEntity<Skill> update() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public ResponseEntity<Skill> patch(Long id, Map<String, String> fields) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public ResponseEntity<Skill> delete(Long id) {
		throw new UnsupportedOperationException();
	}
	
}