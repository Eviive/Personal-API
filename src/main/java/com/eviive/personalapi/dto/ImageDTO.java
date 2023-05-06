package com.eviive.personalapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

@Data
public class ImageDTO {

    private Long id;

    private UUID uuid;

    @NotBlank(message = "The image's alt cannot be blank.")
    @Length(max = 255, message = "The image's alt cannot be longer than 255 characters.")
    private String alt;

}
