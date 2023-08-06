package com.eviive.personalapi.repository;

import com.eviive.personalapi.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findAllByFeaturedIsTrue();

    List<Project> findAllByFeaturedIsFalse();

    Page<Project> findAllByFeaturedIsFalse(Pageable pageable);

    @Transactional
    @Modifying
    @Query("update Project p set p.sort = :sort where p.id = :id")
    void updateSortById(@Param("sort") Integer sort, @Param("id") Long id);

}
