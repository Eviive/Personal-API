package com.eviive.personalapi.controller;

import com.eviive.personalapi.dto.SkillDTO;
import com.eviive.personalapi.service.SkillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("skill")
@RequiredArgsConstructor
public class SkillController {

	private final SkillService skillService;

    @GetMapping(path = "{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<SkillDTO> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(skillService.findById(id));
    }

	@GetMapping(produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<List<SkillDTO>> findAll() {
		return ResponseEntity.ok().body(skillService.findAll());
	}

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<SkillDTO> save(@RequestBody @Valid SkillDTO skillDTO) {
        SkillDTO createdSkillDTO = skillService.save(skillDTO);

        URI uri = URI.create(String.format("/skill/%s", createdSkillDTO.getId()));

        return ResponseEntity.created(uri).body(createdSkillDTO);
    }

    @PutMapping(path = "{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<SkillDTO> update(@PathVariable("id") Long id, @RequestBody @Valid SkillDTO skillDTO) {
        return ResponseEntity.ok().body(skillService.update(id, skillDTO));
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        skillService.delete(id);

        return ResponseEntity.noContent().build();
    }

}
