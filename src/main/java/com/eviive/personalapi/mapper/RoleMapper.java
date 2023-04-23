package com.eviive.personalapi.mapper;

import com.eviive.personalapi.dto.RoleDto;
import com.eviive.personalapi.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.ReportingPolicy.ERROR;

@Mapper(unmappedTargetPolicy = ERROR, componentModel = "spring", injectionStrategy = CONSTRUCTOR)
public interface RoleMapper {

    // to Entity

    @Mapping(target = "users", ignore = true)
    Role toEntity(RoleDto roleDto);

    List<Role> toEntity(List<RoleDto> roleDtos);

    Set<Role> toEntity(Set<RoleDto> roleDtos);

    // to DTO

    RoleDto toDto(Role role);

    List<RoleDto> toDto(List<Role> roles);

    Set<RoleDto> toDto(Set<Role> roles);

}
