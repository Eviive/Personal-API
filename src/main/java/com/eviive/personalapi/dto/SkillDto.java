package com.eviive.personalapi.dto;

import lombok.Data;

@Data
public class SkillDto implements IDto {

    private Long id;

    private String name;

    private ImageDto image;

}
