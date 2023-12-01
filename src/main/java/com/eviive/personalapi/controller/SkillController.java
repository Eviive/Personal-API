package com.eviive.personalapi.controller;

import com.eviive.personalapi.dto.SkillDTO;
import com.eviive.personalapi.service.SkillService;
import com.eviive.personalapi.util.UriUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<SkillDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(skillService.findById(id));
    }

	@GetMapping(produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<List<SkillDTO>> findAll() {
		return ResponseEntity.ok(skillService.findAll());
	}

    // POST

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<SkillDTO> save(@RequestBody @Valid SkillDTO skillDTO) {
        SkillDTO createdSkill = skillService.save(skillDTO, null);
        URI location = uriUtils.buildLocation(createdSkill.getId());
        return ResponseEntity.created(location)
                             .body(createdSkill);
    }

    @PostMapping(path = "with-image", consumes = MULTIPART_FORM_DATA_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<SkillDTO> saveWithImage(@RequestPart("skill") @Valid SkillDTO skillDTO, @RequestPart("file") MultipartFile file) {
        SkillDTO createdSkill = skillService.save(skillDTO, file);
        URI location = uriUtils.buildLocation(createdSkill.getId(), "with-image");
        return ResponseEntity.created(location)
                             .body(createdSkill);
    }

    @PostMapping(path = "sort", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> sort(@RequestBody List<Long> sortedIds) {
        skillService.sort(sortedIds);
        return ResponseEntity.noContent().build();
    }

    // PUT

    @PutMapping(path = "{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<SkillDTO> update(@PathVariable Long id, @RequestBody @Valid SkillDTO skillDTO) {
        return ResponseEntity.ok(skillService.update(id, skillDTO, null));
    }

    @PutMapping(path = "{id}/with-image", consumes = MULTIPART_FORM_DATA_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<SkillDTO> updateWithImage(@PathVariable Long id, @RequestPart("skill") @Valid SkillDTO skillDTO, @RequestPart("file") MultipartFile file) {
        return ResponseEntity.ok(skillService.update(id, skillDTO, file));
    }

    // DELETE

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        skillService.delete(id);

        return ResponseEntity.noContent().build();
    }

}
