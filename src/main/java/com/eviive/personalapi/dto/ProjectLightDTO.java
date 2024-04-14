package com.eviive.personalapi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class ProjectLightDTO {

    private Long id;

    @NotBlank(message = "The project's title cannot be blank.")
    @Length(max = 50, message = "The project's title cannot be longer than {max} characters.")
    private String title;

    @NotNull(message = "The project's featured status is required.")
    private Boolean featured;

    @Min(value = 0, message = "The project's sort cannot be less than {min}.")
    private Integer sort;

}
