package com.eviive.personalapi.service;

import com.eviive.personalapi.dto.ProjectDTO;
import com.eviive.personalapi.entity.Project;
import com.eviive.personalapi.exception.PersonalApiException;
import com.eviive.personalapi.mapper.ProjectMapper;
import com.eviive.personalapi.repository.ProjectRepository;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.eviive.personalapi.exception.PersonalApiErrorsEnum.*;

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

    public Page<ProjectDTO> findAllNotFeaturedPaginated(int page, int size) {
        if (page < 1) {
            throw PersonalApiException.format(API400_PAGE_NUMBER_INVALID, page);
        }

        if (size < 1) {
            throw PersonalApiException.format(API400_PAGE_SIZE_INVALID, size);
        }

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("sort"));

        return projectRepository.findAllByFeaturedIsFalse(pageable)
                                .map(projectMapper::toDTO);
    }

    public ProjectDTO save(ProjectDTO projectDTO, @Nullable MultipartFile file) {
        Project project = projectMapper.toEntity(projectDTO);

        if (file != null) {
            imageService.upload(project.getImage(), Project.AZURE_CONTAINER_NAME, file);
        }

        return projectMapper.toDTO(projectRepository.save(project));
    }

    public void sort(List<Long> sortedIds) {
        for (Long id: sortedIds) {
            projectRepository.updateSortById(sortedIds.indexOf(id), id);
        }
    }

    public ProjectDTO update(Long id, ProjectDTO projectDTO, @Nullable MultipartFile file) {
        if (!projectRepository.existsById(id)) {
            throw PersonalApiException.format(API404_PROJECT_ID_NOT_FOUND, id);
        }

        Project project = projectMapper.toEntity(projectDTO);

        project.setId(id);

        if (file != null) {
            imageService.upload(project.getImage(), Project.AZURE_CONTAINER_NAME, file);
        }

        return projectMapper.toDTO(projectRepository.save(project));
    }

    public void delete(Long id) {
        Project project = projectRepository.findById(id)
                                           .orElseThrow(() -> PersonalApiException.format(API404_PROJECT_ID_NOT_FOUND, id));

        if (project.getImage().getUuid() != null) {
            imageService.delete(project.getImage(), Project.AZURE_CONTAINER_NAME);
        }

        projectRepository.deleteById(id);
    }

}
