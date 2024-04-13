package com.eviive.personalapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RevalidateResponseDTO {

    @NotBlank(message = "The revalidated status is required.")
    private Boolean revalidated;

    @NotBlank(message = "The timestamp is required.")
    private LocalDateTime timestamp;

}
