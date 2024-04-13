package com.eviive.personalapi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.util.Set;

@Data
public class ProjectDTO {

    private Long id;

    @NotBlank(message = "The project's title cannot be blank.")
    @Length(max = 50, message = "The project's title cannot be longer than {max} characters.")
    private String title;

    @NotBlank(message = "The project's english description cannot be blank.")
    @Length(
        max = 510,
        message = "The project's english description cannot be longer than {max} characters."
    )
    private String descriptionEn;

    @NotBlank(message = "The project's french description cannot be blank.")
    @Length(
        max = 510,
        message = "The project's french description cannot be longer than {max} characters."
    )
    private String descriptionFr;

    @NotNull(message = "The project's creation date is required.")
    private LocalDate creationDate;

    @NotBlank(message = "The project's repository URL cannot be blank.")
    @Length(
        max = 255,
        message = "The project's repository URL cannot be longer than {max} characters."
    )
    private String repoUrl;

    @NotBlank(message = "The project's demo URL cannot be blank.")
    @Length(max = 255, message = "The project's demo URL cannot be longer than {max} characters.")
    private String demoUrl;

    @NotNull(message = "The project's featured status is required.")
    private Boolean featured;

    @Min(value = 0, message = "The project's sort cannot be less than {min}.")
    private Integer sort;

    @NotNull(message = "The project's image is required.")
    @Valid
    private ImageDTO image;

    @NotNull(message = "The project's skills is required.")
    @Valid
    private Set<SkillDTO> skills;

}
