package com.eviive.personalapi.mapper;

import com.eviive.personalapi.dto.ProjectDTO;
import com.eviive.personalapi.dto.ProjectLightDTO;
import com.eviive.personalapi.entity.Project;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
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
    uses = {SkillMapper.class, ImageMapper.class}
)
public interface ProjectMapper {

    // to Entity

    Project toEntity(ProjectDTO projectDTO);

    List<Project> toListEntity(Collection<ProjectDTO> projectsDTO);

    Set<Project> toSetEntity(Collection<ProjectDTO> projectsDTO);

    @AfterMapping
    default void afterMapping(@MappingTarget Project project) {
        if (project.getImage() != null) {
            project.getImage().setProject(project);
        }
        if (project.getSkills() != null) {
            project.getSkills().forEach(skill -> skill.getProjects().add(project));
        }
    }

    // to DTO

    ProjectDTO toDTO(Project project);

    List<ProjectDTO> toListDTO(Collection<Project> projects);

    Set<ProjectDTO> toSetDTO(Collection<Project> projects);

    // to Light DTO

    ProjectLightDTO toLightDTO(Project project);

    List<ProjectLightDTO> toLightListDTO(Collection<Project> projects);

    Set<ProjectLightDTO> toLightSetDTO(Collection<Project> projects);

}
