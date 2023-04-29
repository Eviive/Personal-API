package com.eviive.personalapi.dto;

import lombok.Data;

import java.util.Set;

@Data
public class SkillDTO {

    private Long id;

    private String name;

    private ImageDTO image;

    private Set<ProjectDTO> projects;

}
