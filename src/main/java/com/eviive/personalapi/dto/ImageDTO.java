package com.eviive.personalapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class ImageDTO {

    private Long id;

    private UUID uuid;

    @NotBlank(message = "The image's alt cannot be blank.")
    private String alt;

}
