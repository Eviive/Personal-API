package com.eviive.personalapi.mapper;

import com.eviive.personalapi.dto.ProjectDTO;
import com.eviive.personalapi.entity.Project;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.ReportingPolicy.ERROR;

@Mapper(unmappedTargetPolicy = ERROR, componentModel = "spring", injectionStrategy = CONSTRUCTOR, uses = {SkillMapper.class})
public interface ProjectMapper {

    // to Entity

    Project toEntity(ProjectDTO projectDTO);

    List<Project> toEntity(List<ProjectDTO> projectDTOs);

    Set<Project> toEntity(Set<ProjectDTO> projectDTOs);

    // to DTO

    ProjectDTO toDTO(Project project);

    List<ProjectDTO> toDTO(List<Project> projects);

    Set<ProjectDTO> toDTO(Set<Project> projects);

}
