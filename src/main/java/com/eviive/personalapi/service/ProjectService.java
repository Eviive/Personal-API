package com.eviive.personalapi.service;

import com.eviive.personalapi.model.Project;
import com.eviive.personalapi.model.Skill;
import com.eviive.personalapi.repository.ProjectRepository;
import com.eviive.personalapi.repository.SkillRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProjectService extends AbstractService<Project> {
	
	private final SkillRepository skillRepository;
	
	public ProjectService(ProjectRepository projectRepository, SkillRepository skillRepository) {
		super(projectRepository);
		this.skillRepository = skillRepository;
	}
	
	public List<Project> findAllFeatured() {
		return ((ProjectRepository)getRepository()).findAllByFeaturedIsTrue();
	}
	
	public List<Project> findAllNotFeatured() {
		return ((ProjectRepository)getRepository()).findAllByFeaturedIsFalse();
	}
	
	public void addSkillToProject(Long projectId, Long... skillIds) {
		Optional<Project> optProject = findById(projectId);
		
		if (optProject.isPresent()) {
			Optional<Skill> optSkill;
			for (Long skillId: skillIds) {
				optSkill = skillRepository.findById(skillId);
				
				optSkill.ifPresent(s -> optProject.get().addSkill(s));
			}
		}
	}
	
}