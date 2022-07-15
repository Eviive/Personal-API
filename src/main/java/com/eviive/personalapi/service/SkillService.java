package com.eviive.personalapi.service;

import com.eviive.personalapi.model.Skill;
import com.eviive.personalapi.repository.SkillRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SkillService extends AbstractService<Skill> {
	
	public SkillService(SkillRepository skillRepository) {
		super(skillRepository);
	}
	
}