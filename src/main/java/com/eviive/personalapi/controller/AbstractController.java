package com.eviive.personalapi.controller;

import com.eviive.personalapi.mapper.ModelMapper;
import com.eviive.personalapi.model.IModel;
import com.eviive.personalapi.service.AbstractService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Data
@RequiredArgsConstructor
public abstract class AbstractController<E extends IModel> {
	
	private final AbstractService<E> service;
	private final ModelMapper<E> mapper;
	
	@GetMapping(
			produces = APPLICATION_JSON_VALUE
	)
	ResponseEntity<List<E>> findAll() {
		return ResponseEntity.ok().body(service.findAll());
	}
	
	@GetMapping(
			path = "{id}",
			produces = APPLICATION_JSON_VALUE
	)
	ResponseEntity<E> findById(@PathVariable("id") Long id) {
		Optional<E> optElement = service.findById(id);
		
		if (optElement.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok().body(optElement.get());
	}
	
	@PostMapping(
			produces = APPLICATION_JSON_VALUE
	)
	ResponseEntity<E> save(@Valid @RequestBody E element, HttpServletRequest req) {
		E createdElement = service.save(element);
		URI uri = URI.create(req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/projects/" + createdElement.getId());

		return ResponseEntity.created(uri).body(createdElement);
	}
	
	@PutMapping(
			path = "{id}",
			consumes = APPLICATION_JSON_VALUE,
			produces = APPLICATION_JSON_VALUE
	)
	ResponseEntity<E> update(@PathVariable("id") Long id, @Valid @RequestBody E element) {
		return mapElement(id, element);
	}
	
	@PatchMapping(
			path = "{id}",
			consumes = APPLICATION_JSON_VALUE,
			produces = APPLICATION_JSON_VALUE
	)
	ResponseEntity<E> patch(@PathVariable("id") Long id, @RequestBody E element) {
		return mapElement(id, element);
	}
	
	@DeleteMapping(
			path = "{id}",
			produces = APPLICATION_JSON_VALUE
	)
	ResponseEntity<String> delete(@PathVariable("id") Long id) {
		if (service.findById(id).isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		service.deleteById(id);
		return ResponseEntity.noContent().build();
	}
	
	private ResponseEntity<E> mapElement(Long id, E element) {
		Optional<E> optElement = service.findById(id);
		
		if (optElement.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		mapper.map(element, optElement.get());
		
		return ResponseEntity.ok().body(service.save(optElement.get()));
	}
	
}