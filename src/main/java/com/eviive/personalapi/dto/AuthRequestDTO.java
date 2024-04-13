package com.eviive.personalapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthRequestDTO {

    @NotBlank(message = "The username cannot be blank.")
    private String username;

    @NotBlank(message = "The password cannot be blank.")
    private String password;

}
