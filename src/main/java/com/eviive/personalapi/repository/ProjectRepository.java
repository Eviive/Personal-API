package com.eviive.personalapi.repository;

import com.eviive.personalapi.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findAllByFeaturedIsTrue();

    List<Project> findAllByFeaturedIsFalse();

    Page<Project> findAllByFeaturedIsFalse(Pageable pageable);

    @Query("select max(p.sort) from Project p")
    Optional<Integer> findMaxSort();

    @Modifying
    @Query("update Project p set p.sort = :sort where p.id = :id")
    void updateSortById(Long id, Integer sort);

}
