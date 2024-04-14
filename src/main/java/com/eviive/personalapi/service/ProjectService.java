package com.eviive.personalapi.service;

import com.eviive.personalapi.dto.ProjectDTO;
import com.eviive.personalapi.dto.SortUpdateDTO;
import com.eviive.personalapi.entity.Project;
import com.eviive.personalapi.exception.PersonalApiException;
import com.eviive.personalapi.mapper.ProjectMapper;
import com.eviive.personalapi.repository.ProjectRepository;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

import static com.eviive.personalapi.exception.PersonalApiErrorsEnum.API400_PROJECT_ID_NOT_ALLOWED;
import static com.eviive.personalapi.exception.PersonalApiErrorsEnum.API404_PROJECT_ID_NOT_FOUND;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    private final ProjectMapper projectMapper;

    private final ImageService imageService;

    public List<ProjectDTO> findAll() {
        return projectMapper.toListDTO(projectRepository.findAll());
    }

    public List<ProjectDTO> findAllFeatured() {
        return projectMapper.toListDTO(projectRepository.findAllByFeaturedIsTrue());
    }

    public Page<ProjectDTO> findAllNotFeatured(final Pageable pageable) {
        return projectRepository.findAllByFeaturedIsFalse(pageable)
            .map(projectMapper::toDTO);
    }

    public ProjectDTO create(final ProjectDTO projectDTO, @Nullable final MultipartFile file) {
        if (projectDTO.getId() != null) {
            throw new PersonalApiException(API400_PROJECT_ID_NOT_ALLOWED);
        }

        final Project project = projectMapper.toEntity(projectDTO);

        final Integer newSort = projectRepository
            .findMaxSort()
            .map(sort -> sort + 1)
            .orElse(0);

        project.setSort(newSort);

        return save(project, file);
    }

    public ProjectDTO update(final Long id, final ProjectDTO projectDTO, @Nullable final MultipartFile file) {
        if (!projectRepository.existsById(id)) {
            throw PersonalApiException.format(API404_PROJECT_ID_NOT_FOUND, id);
        }

        final Project project = projectMapper.toEntity(projectDTO);

        project.setId(id);

        return save(project, file);
    }

    private ProjectDTO save(final Project project, final @Nullable MultipartFile file) {
        UUID oldUuid = null;
        if (file != null) {
            oldUuid = project.getImage().getUuid();
            project.getImage().setUuid(UUID.randomUUID());
        }

        final Project savedProject = projectRepository.save(project);

        if (file != null) {
            imageService.upload(savedProject.getImage(), oldUuid, file);
        }

        return projectMapper.toDTO(savedProject);
    }

    public void delete(final Long id) {
        final Project project = projectRepository.findById(id)
            .orElseThrow(() -> PersonalApiException.format(API404_PROJECT_ID_NOT_FOUND, id));

        if (project.getImage().getUuid() != null) {
            imageService.delete(project.getImage());
        }

        projectRepository.deleteById(id);
    }

    public void sort(final List<SortUpdateDTO> sorts) {
        for (SortUpdateDTO sort: sorts) {
            projectRepository.updateSortById(sort.getId(), sort.getSort());
        }
    }

}
