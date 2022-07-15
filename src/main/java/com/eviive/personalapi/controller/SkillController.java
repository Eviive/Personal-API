package com.eviive.personalapi.controller;

import com.eviive.personalapi.mapper.SkillMapper;
import com.eviive.personalapi.model.Skill;
import com.eviive.personalapi.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("skills")
@RequiredArgsConstructor
public class SkillController implements BasicController<Skill> {
	
	private final SkillService skillService;
	private final SkillMapper mapper;
	
	@Override
	public ResponseEntity<List<Skill>> findAll() {
		return ResponseEntity.ok().body(skillService.findAll());
	}
	
	@Override
	public ResponseEntity<Skill> findById(Long id) {
		Optional<Skill> optSkill = skillService.findById(id);
		
		if (optSkill.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok().body(optSkill.get());
	}
	
	@Override
	public ResponseEntity<Skill> save(Skill skill, HttpServletRequest req) {
		Skill createdSkill = skillService.save(skill);
		URI uri = URI.create(req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/skills/" + createdSkill.getId());
		
		return ResponseEntity.created(uri).body(createdSkill);
	}
	
	@Override
	public ResponseEntity<Skill> update() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public ResponseEntity<Skill> patch(Long id, Skill skill) {
		Optional<Skill> optSkill = skillService.findById(id);
		
		if (optSkill.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		mapper.map(skill, optSkill.get());
		
		return ResponseEntity.ok().body(skillService.save(optSkill.get()));
	}
	
	@Override
	public ResponseEntity<String> delete(Long id) {
		if (skillService.findById(id).isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		skillService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
	
}