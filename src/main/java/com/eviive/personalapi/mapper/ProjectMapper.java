package com.eviive.personalapi.mapper;

import com.eviive.personalapi.dto.ProjectDto;
import com.eviive.personalapi.entity.Project;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.ReportingPolicy.ERROR;

@Mapper(unmappedTargetPolicy = ERROR, componentModel = "spring", injectionStrategy = CONSTRUCTOR, uses = {SkillMapper.class})
public interface ProjectMapper {

    // to Entity

    Project toEntity(ProjectDto projectDto);

    List<Project> toEntity(List<ProjectDto> projectDtos);

    Set<Project> toEntity(Set<ProjectDto> projectDtos);

    // to DTO

    ProjectDto toDto(Project project);

    List<ProjectDto> toDto(List<Project> projects);

    Set<ProjectDto> toDto(Set<Project> projects);

}
