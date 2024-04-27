package com.eviive.personalapi.dto;

import jakarta.validation.constraints.NotBlank;

public record RevalidateRequestDTO(

    @NotBlank(message = "The secret cannot be blank.")
    String secret,

    String path

) {

}
