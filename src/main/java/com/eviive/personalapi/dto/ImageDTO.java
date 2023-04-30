package com.eviive.personalapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ImageDTO {

    private Long id;

    @NotBlank(message = "The image's alt cannot be blank.")
    private String alt;

}
