package com.eviive.personalapi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SkillDTO {

    private Long id;

    @NotBlank(message = "The skill's name cannot be blank.")
    private String name;

    @NotNull(message = "The skill's image cannot be null.")
    @Valid
    private ImageDTO image;

}
