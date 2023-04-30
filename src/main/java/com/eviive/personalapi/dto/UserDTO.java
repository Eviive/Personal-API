package com.eviive.personalapi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
public class UserDTO {

    private Long id;

    @NotBlank(message = "The user's username cannot be blank.")
    private String username;

    @NotBlank(message = "The user's password cannot be blank.")
    private String firstName;

    @NotBlank(message = "The user's password cannot be blank.")
    private String lastName;

    @NotNull(message = "The user's roles cannot be null.")
    @Valid
    private Set<RoleDTO> roles;

}
