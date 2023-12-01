package com.eviive.personalapi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

@Data
public class UserDTO {

    private Long id;

    @NotBlank(message = "The user's username cannot be blank.")
    @Length(max = 50, message = "The user's username cannot be longer than {max} characters.")
    private String username;

    @NotBlank(message = "The user's password cannot be blank.")
    @Length(max = 50, message = "The user's password cannot be longer than {max} characters.")
    private String firstName;

    @NotBlank(message = "The user's password cannot be blank.")
    @Length(max = 50, message = "The user's password cannot be longer than {max} characters.")
    private String lastName;

    @NotNull(message = "The user's roles cannot be null.")
    @Valid
    private Set<RoleDTO> roles;

}
