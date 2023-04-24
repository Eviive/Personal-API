package com.eviive.personalapi.mapper;

import com.eviive.personalapi.dto.SkillDTO;
import com.eviive.personalapi.entity.Skill;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.ReportingPolicy.ERROR;

@Mapper(unmappedTargetPolicy = ERROR, componentModel = "spring", injectionStrategy = CONSTRUCTOR)
public interface SkillMapper {

    // to Entity

    @Mapping(target = "projects", ignore = true)
    Skill toEntity(SkillDTO skill);

    List<Skill> toEntity(List<SkillDTO> skillDTOs);

    Set<Skill> toEntity(Set<SkillDTO> skillDTOs);

    // to DTO

    SkillDTO toDTO(Skill skill);

    List<SkillDTO> toDTO(List<Skill> skills);

    Set<SkillDTO> toDTO(Set<Skill> skills);

}
