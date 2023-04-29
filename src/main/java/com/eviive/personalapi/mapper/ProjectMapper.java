package com.eviive.personalapi.mapper;

import com.eviive.personalapi.dto.ProjectDTO;
import com.eviive.personalapi.entity.Project;
import org.mapstruct.Mapper;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.ReportingPolicy.ERROR;

@Mapper(unmappedTargetPolicy = ERROR, componentModel = "spring", injectionStrategy = CONSTRUCTOR)
public interface ProjectMapper {

    // to Entity

    Project toEntity(ProjectDTO projectDTO);

    List<Project> toListEntity(Collection<ProjectDTO> projectDTOs);

    Set<Project> toSetEntity(Collection<ProjectDTO> projectDTOs);

    // to DTO

    ProjectDTO toDTO(Project project);

    List<ProjectDTO> toListDTO(Collection<Project> projects);

    Set<ProjectDTO> toSetDTO(Collection<Project> projects);

}
