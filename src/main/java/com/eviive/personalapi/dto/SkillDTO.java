package com.eviive.personalapi.dto;

import lombok.Data;

@Data
public class SkillDTO implements IDTO {

    private Long id;

    private String name;

    private ImageDTO image;

}
