package com.eviive.personalapi.repository;

import com.eviive.personalapi.model.ApiUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<ApiUser, Long> {
	
	Optional<ApiUser> findByUsername(String username);
	
}