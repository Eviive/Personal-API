package com.eviive.personalapi.service;

import com.eviive.personalapi.model.Project;
import com.eviive.personalapi.model.Skill;
import com.eviive.personalapi.repository.ProjectRepository;
import com.eviive.personalapi.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectService implements BasicService<Project> {
	
	private final ProjectRepository projectRepository;
	private final SkillRepository skillRepository;
	
	@Override
	public List<Project> findAll() {
		return projectRepository.findAll();
	}
	
	@Override
	public Optional<Project> findById(Long id) {
		return projectRepository.findById(id);
	}
	
	@Override
	public Project save(Project project) {
		return projectRepository.save(project);
	}
	
	@Override
	public void deleteById(Long id) {
		projectRepository.deleteById(id);
	}
	
	public List<Project> findAllFeatured() {
		return projectRepository.findAllByFeaturedIsTrue();
	}
	
	public List<Project> findAllNotFeatured() {
		return projectRepository.findAllByFeaturedIsFalse();
	}
	
	public void addSkillToProject(Long projectId, Long... skillIds) {
		Optional<Project> project = findById(projectId);
		
		if (project.isPresent()) {
			Optional<Skill> optSkill;
			for (Long skillId: skillIds) {
				optSkill = skillRepository.findById(skillId);
				
				optSkill.ifPresent(project.get()::addSkill);
			}
		}
	}
	
}