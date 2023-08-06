package com.eviive.personalapi.mapper;

import com.eviive.personalapi.dto.SkillDTO;
import com.eviive.personalapi.entity.Skill;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.ReportingPolicy.ERROR;

@Mapper(
        unmappedTargetPolicy = ERROR,
        componentModel = "spring",
        injectionStrategy = CONSTRUCTOR,
        uses = {ImageMapper.class}
)
public interface SkillMapper {

    // to Entity

    @Mapping(target = "projects", ignore = true)
    Skill toEntity(SkillDTO skill);

    List<Skill> toListEntity(Collection<SkillDTO> skillDTOs);

    Set<Skill> toSetEntity(Collection<SkillDTO> skillDTOs);

    @AfterMapping
    default void afterMapping(@MappingTarget Skill skill) {
        if (skill.getProjects() != null) {
            skill.getProjects().forEach(project -> project.getSkills().add(skill));
        }
        if (skill.getImage() != null) {
            skill.getImage().setSkill(skill);
        }
    }

    // to DTO

    SkillDTO toDTO(Skill skill);

    List<SkillDTO> toListDTO(Collection<Skill> skills);

    Set<SkillDTO> toSetDTO(Collection<Skill> skills);

}
