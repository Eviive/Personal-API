package com.eviive.personalapi.service;

import com.eviive.personalapi.model.IModel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Data
@RequiredArgsConstructor
public abstract class AbstractService<E extends IModel> {
	
	private final JpaRepository<E, Long> repository;
	
	public List<E> findAll() {
		return repository.findAll();
	}
	
	public Optional<E> findById(Long id) {
		return repository.findById(id);
	}
	
	public E save(@Valid E element) {
		return repository.save(element);
	}
	
	public void deleteById(Long id) {
		repository.deleteById(id);
	}
	
}