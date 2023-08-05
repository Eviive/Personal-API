package com.eviive.personalapi.controller;

import com.eviive.personalapi.dto.ProjectDTO;
import com.eviive.personalapi.exception.PersonalApiException;
import com.eviive.personalapi.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;

import static com.eviive.personalapi.exception.PersonalApiErrorsEnum.API400_PAGE_NUMBER_NEGATIVE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("project")
@RequiredArgsConstructor
public class ProjectController {

	private final ProjectService projectService;

    // GET

    @GetMapping(path = "{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ProjectDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(projectService.findById(id));
    }

	@GetMapping(produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ProjectDTO>> findAll() {
		return ResponseEntity.ok().body(projectService.findAll());
	}

	@GetMapping(path = "featured", produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ProjectDTO>> findAllFeatured() {
		return ResponseEntity.ok().body(projectService.findAllFeatured());
	}

    @GetMapping(path = "not-featured", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProjectDTO>> findAllNotFeatured() {
        return ResponseEntity.ok().body(projectService.findAllNotFeatured());
    }

	@GetMapping(path = "not-featured/paginated", produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<Iterable<ProjectDTO>> findAllNotFeaturedPaginated(@RequestParam(value = "page", defaultValue = "0") Integer page) {
        if (page < 0) {
            throw PersonalApiException.format(API400_PAGE_NUMBER_NEGATIVE, page);
        }

        return ResponseEntity.ok().body(projectService.findAllNotFeaturedPaginated(page));
	}

    // POST

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ProjectDTO> save(@RequestBody @Valid ProjectDTO projectDTO) {
        ProjectDTO createdProjectDTO = projectService.save(projectDTO, null);

        URI uri = URI.create(String.format("/project/%s", createdProjectDTO.getId()));

        return ResponseEntity.created(uri).body(createdProjectDTO);
    }

    @PostMapping(path = "with-image", consumes = MULTIPART_FORM_DATA_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ProjectDTO> saveWithImage(@RequestPart("project") @Valid ProjectDTO projectDTO, @RequestPart("file") MultipartFile file) {
        ProjectDTO createdProjectDTO = projectService.save(projectDTO, file);

        URI uri = URI.create(String.format("/project/%s", createdProjectDTO.getId()));

        return ResponseEntity.created(uri).body(createdProjectDTO);
    }

    @PostMapping(path = "sort", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> sort(@RequestBody List<Long> sortedIds) {
        projectService.sort(sortedIds);
        return ResponseEntity.noContent().build();
    }

    // PUT

    @PutMapping(path = "{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ProjectDTO> update(@PathVariable Long id, @RequestBody @Valid ProjectDTO projectDTO) {
        return ResponseEntity.ok().body(projectService.update(id, projectDTO, null));
    }

    @PutMapping(path = "{id}/with-image", consumes = MULTIPART_FORM_DATA_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ProjectDTO> updateWithImage(@PathVariable Long id, @RequestPart("project") @Valid ProjectDTO projectDTO, @RequestPart("file") MultipartFile file) {
        return ResponseEntity.ok().body(projectService.update(id, projectDTO, file));
    }

    // DELETE

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        projectService.delete(id);

        return ResponseEntity.noContent().build();
    }

}
