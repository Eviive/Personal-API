package com.eviive.personalapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AuthResponseDTO(

    @NotNull(message = "The current user cannot be null.")
    CurrentUserDTO currentUser,

    @NotBlank(message = "The access token cannot be blank.")
    String accessToken

) {

}
