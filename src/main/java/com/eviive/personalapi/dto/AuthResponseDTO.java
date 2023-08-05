package com.eviive.personalapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class AuthResponseDTO {

    @NotBlank(message = "The username is required.")
    private String username;

    @NotNull(message = "Roles are required.")
    private List<String> roles;

    @NotBlank(message = "The access token is required.")
    private String accessToken;

}
