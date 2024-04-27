package com.eviive.personalapi.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthResponseDTO(

    @NotBlank(message = "The access token cannot be blank.")
    String accessToken

) {

}
