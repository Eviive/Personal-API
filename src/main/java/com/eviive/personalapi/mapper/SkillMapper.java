package com.eviive.personalapi.mapper;

import com.eviive.personalapi.dto.SkillDTO;
import com.eviive.personalapi.entity.Skill;
import org.mapstruct.Mapper;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.ReportingPolicy.ERROR;

@Mapper(unmappedTargetPolicy = ERROR, componentModel = "spring", injectionStrategy = CONSTRUCTOR)
public interface SkillMapper {

    // to Entity

    Skill toEntity(SkillDTO skill);

    List<Skill> toListEntity(Collection<SkillDTO> skillDTOs);

    Set<Skill> toSetEntity(Collection<SkillDTO> skillDTOs);

    // to DTO

    SkillDTO toDTO(Skill skill);

    List<SkillDTO> toListDTO(Collection<Skill> skills);

    Set<SkillDTO> toSetDTO(Collection<Skill> skills);

}
