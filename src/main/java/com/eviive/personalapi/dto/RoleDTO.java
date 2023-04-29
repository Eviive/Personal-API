package com.eviive.personalapi.dto;

import lombok.Data;

import java.util.Set;

@Data
public class RoleDTO {

    private Long id;

    private String name;

    private Set<UserDTO> users;

}
