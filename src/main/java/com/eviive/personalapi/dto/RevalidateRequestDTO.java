package com.eviive.personalapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RevalidateRequestDTO {

    @NotBlank(message = "The secret is required.")
    private String secret;

    private String path;

}
