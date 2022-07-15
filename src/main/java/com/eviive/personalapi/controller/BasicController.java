package com.eviive.personalapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public interface BasicController<E> {
	
	@GetMapping(
			produces = APPLICATION_JSON_VALUE
	)
	ResponseEntity<List<E>> findAll();
	
	@GetMapping(
			path = "{id}",
			produces = APPLICATION_JSON_VALUE
	)
	ResponseEntity<E> findById(@PathVariable("id") Long id);
	
	@PostMapping(
			produces = APPLICATION_JSON_VALUE
	)
	ResponseEntity<E> save(@RequestBody E element, HttpServletRequest req);
	
	@PutMapping(
			consumes = APPLICATION_JSON_VALUE,
			produces = APPLICATION_JSON_VALUE
	)
	ResponseEntity<E> update();
	
	@PatchMapping(
			path = "{id}",
			consumes = APPLICATION_JSON_VALUE,
			produces = APPLICATION_JSON_VALUE
	)
	ResponseEntity<E> patch(@PathVariable("id") Long id, @RequestBody E element);
	
	@DeleteMapping(
			path = "{id}",
			produces = APPLICATION_JSON_VALUE
	)
	ResponseEntity<String> delete(@PathVariable("id") Long id);
	
}