package com.eviive.personalapi.repository;

import com.eviive.personalapi.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
	
	Optional<Project> findByName(String name);
	
	List<Project> findAllByFeaturedIsTrue();
	
	List<Project> findAllByFeaturedIsFalse();
	
}