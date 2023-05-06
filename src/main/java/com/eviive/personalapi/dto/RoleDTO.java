package com.eviive.personalapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class RoleDTO {

    private Long id;

    @NotBlank(message = "The role's name cannot be blank.")
    @Length(max = 50, message = "The role's name cannot be longer than 50 characters.")
    private String name;

}
