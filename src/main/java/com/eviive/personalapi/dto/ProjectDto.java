package com.eviive.personalapi.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class ProjectDto implements IDto {

    private Long id;

    private String name;

    private String description;

    private LocalDate creationDate;

    private String repoUrl;

    private String demoUrl;

    private Boolean featured;

    private ImageDto image;

    private Set<SkillDto> skills;

}
