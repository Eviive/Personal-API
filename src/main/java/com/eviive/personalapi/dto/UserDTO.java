package com.eviive.personalapi.dto;

import lombok.Data;

import java.util.Set;

@Data
public class UserDTO {

    private Long id;

    private String username;

    private String firstName;

    private String lastName;

    private Set<RoleDTO> roles;

}
