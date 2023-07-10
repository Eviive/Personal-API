package com.eviive.personalapi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
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

    @NotNull(message = "The skill's sort cannot be null.")
    @Min(value = 0, message = "The skill's sort cannot be less than 0.")
    private Integer sort;

    @NotNull(message = "The skill's image cannot be null.")
    @Valid
    private ImageDTO image;

}
