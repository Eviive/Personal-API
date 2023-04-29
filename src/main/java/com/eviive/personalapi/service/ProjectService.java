package com.eviive.personalapi.service;

import com.eviive.personalapi.dto.ProjectDTO;
import com.eviive.personalapi.entity.Project;
import com.eviive.personalapi.exception.PersonalApiException;
import com.eviive.personalapi.mapper.ProjectMapper;
import com.eviive.personalapi.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.eviive.personalapi.exception.PersonalApiErrorsEnum.API404_PROJECT_ID_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    public ProjectDTO findById(Long id) {
        Project project = projectRepository.findById(id)
                                           .orElseThrow(() -> PersonalApiException.format(API404_PROJECT_ID_NOT_FOUND, id));
        return projectMapper.toDTO(project);
    }

    public List<ProjectDTO> findAllOrdered() {
        return projectMapper.toListDTO(projectRepository.findAllByOrderByFeaturedDescCreationDateDesc());
    }

    public List<ProjectDTO> findAllFeatured() {
        return projectMapper.toListDTO(projectRepository.findAllByFeaturedIsTrueOrderByCreationDateDesc());
    }

    public List<ProjectDTO> findAllNotFeatured() {
        return projectMapper.toListDTO(projectRepository.findAllByFeaturedIsFalseOrderByCreationDateDesc());
    }

    public Page<ProjectDTO> findAllNotFeaturedPaginated(int page) {
        Pageable pageable = PageRequest.of(page, 6);

        return projectRepository.findAllByFeaturedIsFalseOrderByCreationDateDesc(pageable)
                                .map(projectMapper::toDTO);
    }

    public ProjectDTO save(ProjectDTO projectDTO) {
        Project project = projectMapper.toEntity(projectDTO);

        return projectMapper.toDTO(projectRepository.save(project));
    }

    public ProjectDTO update(Long id, ProjectDTO projectDTO) {
        if (!projectRepository.existsById(id)) {
            throw PersonalApiException.format(API404_PROJECT_ID_NOT_FOUND, id);
        }

        Project project = projectMapper.toEntity(projectDTO);

        project.setId(id);

        return projectMapper.toDTO(projectRepository.save(project));
    }

    public void delete(Long id) {
        if (!projectRepository.existsById(id)) {
            throw PersonalApiException.format(API404_PROJECT_ID_NOT_FOUND, id);
        }

        projectRepository.deleteById(id);
    }

}
