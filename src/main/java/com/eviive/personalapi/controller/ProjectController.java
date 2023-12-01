package com.eviive.personalapi.controller;

import com.eviive.personalapi.dto.ProjectDTO;
import com.eviive.personalapi.service.ProjectService;
import com.eviive.personalapi.util.UriUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    private final UriUtils uriUtils;

    // GET

    @GetMapping(path = "{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ProjectDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.findById(id));
    }

	@GetMapping(produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ProjectDTO>> findAll() {
		return ResponseEntity.ok(projectService.findAll());
	}

	@GetMapping(path = "featured", produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ProjectDTO>> findAllFeatured() {
		return ResponseEntity.ok(projectService.findAllFeatured());
	}

    @GetMapping(path = "not-featured", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProjectDTO>> findAllNotFeatured() {
        return ResponseEntity.ok(projectService.findAllNotFeatured());
    }

	@GetMapping(path = "not-featured/paginated", produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<ProjectDTO>> findAllNotFeaturedPaginated(Pageable pageable) {
        return ResponseEntity.ok(projectService.findAllNotFeaturedPaginated(pageable));
	}

    // POST

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ProjectDTO> save(@RequestBody @Valid ProjectDTO projectDTO) {
        ProjectDTO createdProject = projectService.save(projectDTO, null);
        URI location = uriUtils.buildLocation(createdProject.getId());
        return ResponseEntity.created(location)
                             .body(createdProject);
    }

    @PostMapping(path = "with-image", consumes = MULTIPART_FORM_DATA_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ProjectDTO> saveWithImage(@RequestPart("project") @Valid ProjectDTO projectDTO, @RequestPart("file") MultipartFile file) {
        ProjectDTO createdProject = projectService.save(projectDTO, file);
        URI location = uriUtils.buildLocation(createdProject.getId(), "with-image");
        return ResponseEntity.created(location)
                             .body(createdProject);
    }

    @PostMapping(path = "sort", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> sort(@RequestBody List<Long> sortedIds) {
        projectService.sort(sortedIds);
        return ResponseEntity.noContent().build();
    }

    // PUT

    @PutMapping(path = "{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ProjectDTO> update(@PathVariable Long id, @RequestBody @Valid ProjectDTO projectDTO) {
        return ResponseEntity.ok(projectService.update(id, projectDTO, null));
    }

    @PutMapping(path = "{id}/with-image", consumes = MULTIPART_FORM_DATA_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ProjectDTO> updateWithImage(@PathVariable Long id, @RequestPart("project") @Valid ProjectDTO projectDTO, @RequestPart("file") MultipartFile file) {
        return ResponseEntity.ok(projectService.update(id, projectDTO, file));
    }

    // DELETE

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        projectService.delete(id);

        return ResponseEntity.noContent().build();
    }

}
