package com.eviive.personalapi.repository;

import com.eviive.personalapi.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    Optional<Image> findByUuid(UUID uuid);

}
