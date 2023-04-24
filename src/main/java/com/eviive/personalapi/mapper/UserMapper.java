package com.eviive.personalapi.mapper;

import com.eviive.personalapi.dto.UserDTO;
import com.eviive.personalapi.entity.User;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.ReportingPolicy.ERROR;

@Mapper(unmappedTargetPolicy = ERROR, componentModel = "spring", injectionStrategy = CONSTRUCTOR, uses = {RoleMapper.class})
public interface UserMapper {

    // to Entity

    User toEntity(UserDTO userDTO);

    List<User> toEntity(List<UserDTO> userDTOs);

    Set<User> toEntity(Set<UserDTO> userDTOs);

    // to DTO

    UserDTO toDTO(User user);

    List<UserDTO> toDTO(List<User> users);

    Set<UserDTO> toDTO(Set<User> users);

}
