package com.eviive.personalapi.mapper;

import com.eviive.personalapi.dto.RoleDTO;
import com.eviive.personalapi.entity.Role;
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
        injectionStrategy = CONSTRUCTOR
)
public interface RoleMapper {

    // to Entity

    @Mapping(target = "users", ignore = true)
    Role toEntity(RoleDTO roleDTO);

    List<Role> toListEntity(Collection<RoleDTO> roleDTOs);

    Set<Role> toSetEntity(Collection<RoleDTO> roleDTOs);

    @AfterMapping
    default void afterMapping(@MappingTarget Role role) {
        if (role.getUsers() != null) {
            role.getUsers().forEach(user -> user.getRoles().add(role));
        }
    }

    // to DTO

    RoleDTO toDTO(Role role);

    List<RoleDTO> toListDTO(Collection<Role> roles);

    Set<RoleDTO> toSetDTO(Collection<Role> roles);

}
