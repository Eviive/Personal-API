package com.eviive.personalapi.repository;

import com.eviive.personalapi.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findAllByOrderByFeaturedDescCreationDateDesc();

    List<Project> findAllByFeaturedIsTrueOrderByCreationDateDesc();

    List<Project> findAllByFeaturedIsFalseOrderByCreationDateDesc();

    Page<Project> findAllByFeaturedIsFalseOrderByCreationDateDesc(Pageable pageable);

}
