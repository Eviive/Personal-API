package com.eviive.personalapi.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record RevalidateResponseDTO(

    @NotNull(message = "The revalidated status is required.")
    Boolean revalidated,

    @NotNull(message = "The timestamp is required.")
    LocalDateTime timestamp

) {

}
