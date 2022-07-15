package com.eviive.personalapi.service;

import java.util.List;
import java.util.Optional;

public interface BasicService<E> {
	
	List<E> findAll();
	
	Optional<E> findById(Long id);
	
	E save(E element);
	
	void deleteById(Long id);
	
}