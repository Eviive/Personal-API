package com.eviive.personalapi.mapper;

import com.eviive.personalapi.dto.SkillDto;
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
    Skill toEntity(SkillDto skill);

    List<Skill> toEntity(List<SkillDto> skills);

    Set<Skill> toEntity(Set<SkillDto> skills);

    // to DTO

    SkillDto toDto(Skill skill);

    List<SkillDto> toDto(List<Skill> skills);

    Set<SkillDto> toDto(Set<Skill> skills);

}
