package com.eviive.personalapi.mapper;

import com.eviive.personalapi.dto.UserDTO;
import com.eviive.personalapi.entity.User;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.ReportingPolicy.ERROR;

@Mapper(
        unmappedTargetPolicy = ERROR,
        componentModel = "spring",
        injectionStrategy = CONSTRUCTOR,
        uses = {RoleMapper.class}
)
public interface UserMapper {

    // to Entity

    @Mapping(target = "password", ignore = true)
    User toEntity(UserDTO userDTO);

    List<User> toListEntity(Collection<UserDTO> userDTOs);

    Set<User> toSetEntity(Collection<UserDTO> userDTOs);

    @AfterMapping
    default void afterMapping(@MappingTarget User user) {
        if (user.getRoles() != null) {
            user.getRoles().forEach(role -> role.getUsers().add(user));
        }
    }

    // to DTO

    UserDTO toDTO(User user);

    List<UserDTO> toListDTO(Collection<User> users);

    Set<UserDTO> toSetDTO(Collection<User> users);

}
