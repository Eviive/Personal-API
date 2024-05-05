package com.eviive.personalapi.mapper;

import com.eviive.personalapi.dto.SkillDTO;
import com.eviive.personalapi.entity.Skill;
import com.eviive.personalapi.mapper.util.CollectionsMapper;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.ReportingPolicy.ERROR;

@Mapper(
    unmappedTargetPolicy = ERROR,
    componentModel = SPRING,
    injectionStrategy = CONSTRUCTOR,
    uses = ImageMapper.class
)
public interface SkillMapper extends CollectionsMapper<Skill, SkillDTO> {

    // to Entity

    @Mapping(target = "projects", ignore = true)
    Skill toEntity(SkillDTO skill);

    @AfterMapping
    default void afterMapping(@MappingTarget Skill skill) {
        if (skill.getImage() != null) {
            skill.getImage().setSkill(skill);
        }
        if (skill.getProjects() != null) {
            skill.getProjects().forEach(project -> project.getSkills().add(skill));
        }
    }

    // to DTO

    SkillDTO toDTO(Skill skill);

}
