package com.eviive.personalapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class RevalidateResponseDTO {

    @NotBlank(message = "The revalidated status is required.")
    private Boolean revalidated;

    @NotBlank(message = "The timestamp is required.")
    private Timestamp timestamp;

}
