package com.eviive.personalapi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class SkillDTO {

    private Long id;

    @NotBlank(message = "The skill's name cannot be blank.")
    @Length(max = 50, message = "The skill's name cannot be longer than 50 characters.")
    private String name;

    @NotNull(message = "The skill's image cannot be null.")
    @Valid
    private ImageDTO image;

}
