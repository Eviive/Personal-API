package com.eviive.personalapi.repository;

import com.eviive.personalapi.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {

    @Transactional
    @Modifying
    @Query("update Skill s set s.sort = :sort where s.id = :id")
    void updateSortById(@Param("sort") Integer sort, @Param("id") Long id);

}
