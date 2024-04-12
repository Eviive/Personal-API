package com.eviive.personalapi.controller;

import com.eviive.personalapi.dto.ProjectDTO;
import com.eviive.personalapi.dto.SortUpdateDTO;
import com.eviive.personalapi.service.ProjectService;
import com.eviive.personalapi.util.UriUtilities;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    private final UriUtilities uriUtilities;

    // GET

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProjectDTO>> findAll() {
        return ResponseEntity.ok(projectService.findAll());
    }

    @GetMapping(path = "featured", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProjectDTO>> findAllFeatured() {
        return ResponseEntity.ok(projectService.findAllFeatured());
    }

    @GetMapping(path = "not-featured", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<ProjectDTO>> findAllNotFeatured(final Pageable pageable) {
        return ResponseEntity.ok(projectService.findAllNotFeatured(pageable));
    }

    // POST

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ProjectDTO> save(@RequestBody @Valid final ProjectDTO projectDTO) {
        final ProjectDTO createdProject = projectService.save(projectDTO, null);
        final URI location = uriUtilities.buildLocation(createdProject.getId());
        return ResponseEntity.created(location)
            .body(createdProject);
    }

    @PostMapping(
        path = "with-image",
        consumes = MULTIPART_FORM_DATA_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ProjectDTO> saveWithImage(
        @RequestPart("project") @Valid final ProjectDTO projectDTO,
        @RequestPart("file") final MultipartFile file
    ) {
        final ProjectDTO createdProject = projectService.save(projectDTO, file);
        final URI location = uriUtilities.buildLocation(createdProject.getId(), "with-image");
        return ResponseEntity.created(location)
            .body(createdProject);
    }

    // PUT

    @PutMapping(path = "{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ProjectDTO> update(
        @PathVariable final Long id,
        @RequestBody @Valid final ProjectDTO projectDTO
    ) {
        return ResponseEntity.ok(projectService.update(id, projectDTO, null));
    }

    @PutMapping(
        path = "{id}/with-image",
        consumes = MULTIPART_FORM_DATA_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ProjectDTO> updateWithImage(
        @PathVariable final Long id,
        @RequestPart("project") final @Valid ProjectDTO projectDTO,
        @RequestPart("file") final MultipartFile file
    ) {
        return ResponseEntity.ok(projectService.update(id, projectDTO, file));
    }

    // PATCH

    @PatchMapping(path = "sort", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> sort(@RequestBody final List<SortUpdateDTO> sorts) {
        projectService.sort(sorts);
        return ResponseEntity.noContent().build();
    }

    // DELETE

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        projectService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
