package com.eviive.personalapi.controller;

import com.eviive.personalapi.dto.SkillDTO;
import com.eviive.personalapi.dto.SortUpdateDTO;
import com.eviive.personalapi.service.SkillService;
import com.eviive.personalapi.util.UriUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("skill")
@RequiredArgsConstructor
public class SkillController {

    private final SkillService skillService;

    private final UriUtils uriUtils;

    // GET

    @GetMapping(path = "{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<SkillDTO> findById(@PathVariable final Long id) {
        return ResponseEntity.ok(skillService.findById(id));
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SkillDTO>> findAll() {
        return ResponseEntity.ok(skillService.findAll());
    }

    // POST

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<SkillDTO> save(@RequestBody @Valid final SkillDTO skillDTO) {
        final SkillDTO createdSkill = skillService.save(skillDTO, null);
        final URI location = uriUtils.buildLocation(createdSkill.getId());
        return ResponseEntity.created(location)
            .body(createdSkill);
    }

    @PostMapping(
        path = "with-image",
        consumes = MULTIPART_FORM_DATA_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SkillDTO> saveWithImage(
        @RequestPart("skill") @Valid final SkillDTO skillDTO,
        @RequestPart("file") final MultipartFile file
    ) {
        final SkillDTO createdSkill = skillService.save(skillDTO, file);
        final URI location = uriUtils.buildLocation(createdSkill.getId(), "with-image");
        return ResponseEntity.created(location)
            .body(createdSkill);
    }

    // PUT

    @PutMapping(path = "{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<SkillDTO> update(
        @PathVariable final Long id,
        @RequestBody @Valid final SkillDTO skillDTO
    ) {
        return ResponseEntity.ok(skillService.update(id, skillDTO, null));
    }

    @PutMapping(
        path = "{id}/with-image",
        consumes = MULTIPART_FORM_DATA_VALUE,
        produces = APPLICATION_JSON_VALUE
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
    public ResponseEntity<Void> sort(@RequestBody final List<SortUpdateDTO> sorts) {
        skillService.sort(sorts);
        return ResponseEntity.noContent().build();
    }

    // DELETE

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        skillService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
