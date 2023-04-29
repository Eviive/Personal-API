package com.eviive.personalapi.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class ProjectDTO {

    private Long id;

    private String title;

    private String description;

    private LocalDate creationDate;

    private String repoUrl;

    private String demoUrl;

    private Boolean featured;

    private ImageDTO image;

    private Set<SkillDTO> skills;

}
