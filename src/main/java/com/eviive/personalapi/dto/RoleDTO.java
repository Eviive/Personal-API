package com.eviive.personalapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RoleDTO {

    private Long id;

    @NotBlank(message = "The role's name cannot be blank.")
    private String name;

}
