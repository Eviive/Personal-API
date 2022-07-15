package com.eviive.personalapi.controller;

import com.eviive.personalapi.mapper.ModelMapper;
import com.eviive.personalapi.model.Skill;
import com.eviive.personalapi.service.AbstractService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("skills")
public class SkillController extends AbstractController<Skill> {
	
	public SkillController(AbstractService<Skill> service) {
		super(service, new ModelMapper<>(Skill.class));
	}
	
}