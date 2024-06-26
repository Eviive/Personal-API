package com.eviive.personalapi.controller;

import com.eviive.personalapi.dto.SkillDTO;
import com.eviive.personalapi.dto.SortUpdateDTO;
import com.eviive.personalapi.service.SkillService;
import com.eviive.personalapi.util.UriUtilities;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("skill")
@RequiredArgsConstructor
@Tag(name = "SkillController")
public class SkillController {

    private final SkillService skillService;

    private final UriUtilities uriUtilities;

    // GET

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Find all skills",
        responses = @ApiResponse(responseCode = "200", description = "OK")
    )
    public ResponseEntity<List<SkillDTO>> findAll() {
        return ResponseEntity.ok(skillService.findAll());
    }

    @GetMapping(path = "slice", produces = APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Find a slice of skills",
        responses = @ApiResponse(responseCode = "200", description = "OK")
    )
    public ResponseEntity<Slice<SkillDTO>> findAll(
        @SortDefault("sort") final Pageable pageable,
        @RequestParam(required = false) final String search
    ) {
        return ResponseEntity.ok(skillService.findAll(pageable, search));
    }

    // POST

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Create a skill",
        responses = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
        }
    )
    public ResponseEntity<SkillDTO> save(@RequestBody @Valid final SkillDTO skillDTO) {
        final SkillDTO createdSkill = skillService.create(skillDTO, null);
        final URI location = uriUtilities.buildLocation(createdSkill.id());
        return ResponseEntity.created(location)
            .body(createdSkill);
    }

    @PostMapping(path = "with-image", consumes = MULTIPART_FORM_DATA_VALUE, produces = APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Create a skill with an image",
        responses = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "415", description = "Unsupported Media Type")
        }
    )
    public ResponseEntity<SkillDTO> saveWithImage(
        @RequestPart("skill") @Valid final SkillDTO skillDTO,
        @RequestPart("file") final MultipartFile file
    ) {
        final SkillDTO createdSkill = skillService.create(skillDTO, file);
        final URI location = uriUtilities.buildLocation(createdSkill.id(), "with-image");
        return ResponseEntity.created(location)
            .body(createdSkill);
    }

    // PUT

    @PutMapping(path = "{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Update a skill",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "404", description = "Not Found")
        }
    )
    public ResponseEntity<SkillDTO> update(
        @PathVariable final Long id,
        @RequestBody @Valid final SkillDTO skillDTO
    ) {
        return ResponseEntity.ok(skillService.update(id, skillDTO, null));
    }

    @PutMapping(path = "{id}/with-image", consumes = MULTIPART_FORM_DATA_VALUE, produces = APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Update a skill with an image",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "415", description = "Unsupported Media Type")
        }
    )
    public ResponseEntity<SkillDTO> updateWithImage(
        @PathVariable final Long id,
        @RequestPart("skill") @Valid final SkillDTO skillDTO,
        @RequestPart("file") final MultipartFile file
    ) {
        return ResponseEntity.ok(skillService.update(id, skillDTO, file));
    }

    // PATCH

    @PatchMapping(path = "sort", consumes = APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Sort skills",
        responses = @ApiResponse(responseCode = "204", description = "No Content")
    )
    public ResponseEntity<Void> sort(@RequestBody final List<SortUpdateDTO> sorts) {
        skillService.sort(sorts);
        return ResponseEntity.noContent().build();
    }

    // DELETE

    @DeleteMapping(path = "{id}")
    @Operation(
        summary = "Delete a skill",
        responses = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "404", description = "Not Found")
        }
    )
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        skillService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
