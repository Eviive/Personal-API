package com.eviive.personalapi.repository;

import com.eviive.personalapi.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {

    @Query("select max(s.sort) from Skill s")
    Optional<Integer> findMaxSort();

    @Modifying
    @Query("update Skill s set s.sort = :sort where s.id = :id")
    void updateSortById(Long id, Integer sort);

}
