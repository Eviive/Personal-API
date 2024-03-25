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

import static com.eviive.personalapi.exception.PersonalApiErrorsEnum.API404_PROJECT_ID_NOT_FOUND;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    private final ImageService imageService;

    public ProjectDTO findById(Long id) {
        Project project = projectRepository.findById(id)
                                           .orElseThrow(() -> PersonalApiException.format(API404_PROJECT_ID_NOT_FOUND, id));
        return projectMapper.toDTO(project);
    }

    public List<ProjectDTO> findAll() {
        return projectMapper.toListDTO(projectRepository.findAll());
    }

    public List<ProjectDTO> findAllFeatured() {
        return projectMapper.toListDTO(projectRepository.findAllByFeaturedIsTrue());
    }

    public List<ProjectDTO> findAllNotFeatured() {
        return projectMapper.toListDTO(projectRepository.findAllByFeaturedIsFalse());
    }

    public Page<ProjectDTO> findAllNotFeaturedPaginated(Pageable pageable) {
        return projectRepository.findAllByFeaturedIsFalse(pageable)
                                .map(projectMapper::toDTO);
    }

    public ProjectDTO save(ProjectDTO projectDTO, @Nullable MultipartFile file) {
        Project project = projectMapper.toEntity(projectDTO);

        Integer newSort = projectRepository
                .findMaxSort()
                .map(sort -> sort + 1)
                .orElse(0);

        project.setSort(newSort);

        return saveOrUpdate(project, file);
    }

    private ProjectDTO saveOrUpdate(Project project, @Nullable MultipartFile file) {
        UUID oldUuid = null;
        if (file != null) {
            oldUuid = project.getImage().getUuid();
            project.getImage().setUuid(UUID.randomUUID());
        }

        Project savedProject = projectRepository.save(project);

        if (file != null) {
            imageService.upload(savedProject.getImage(), oldUuid, file);
        }

        return projectMapper.toDTO(savedProject);
    }

    public void sort(List<SortUpdateDTO> sorts) {
        for (SortUpdateDTO sort: sorts) {
            projectRepository.updateSortById(sort.getId(), sort.getSort());
        }
    }

    public ProjectDTO update(Long id, ProjectDTO projectDTO, @Nullable MultipartFile file) {
        if (!projectRepository.existsById(id)) {
            throw PersonalApiException.format(API404_PROJECT_ID_NOT_FOUND, id);
        }

        Project project = projectMapper.toEntity(projectDTO);

        project.setId(id);

        return saveOrUpdate(project, file);
    }

    public void delete(Long id) {
        Project project = projectRepository.findById(id)
                                           .orElseThrow(() -> PersonalApiException.format(API404_PROJECT_ID_NOT_FOUND, id));

        if (project.getImage().getUuid() != null) {
            imageService.delete(project.getImage());
        }

        projectRepository.deleteById(id);
    }

}
