package com.eviive.personalapi.dto;

import lombok.Data;

import java.util.Set;

@Data
public class UserDTO implements IDTO {

    private Long id;

    private String name;

    private String firstName;

    private String lastName;

    private String password;

    private Set<RoleDTO> roles;

}
