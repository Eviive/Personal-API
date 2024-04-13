package com.eviive.personalapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDTO {

    @NotBlank(message = "The access token cannot be blank.")
    private String accessToken;

}
