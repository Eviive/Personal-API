package com.eviive.personalapi.service;

import com.eviive.personalapi.model.Skill;
import com.eviive.personalapi.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class SkillService {
	
	private final SkillRepository skillRepository;
	
	public List<Skill> findAll() {
		return skillRepository.findAll();
	}
	
	public Optional<Skill> findById(Long id) {
		return skillRepository.findById(id);
	}
	
	public Skill save(Skill skill) {
		return skillRepository.save(skill);
	}
	
}