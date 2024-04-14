package com.eviive.personalapi.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RevalidateResponseDTO {

    @NotNull(message = "The revalidated status is required.")
    private Boolean revalidated;

    @NotNull(message = "The timestamp is required.")
    private LocalDateTime timestamp;

}
