package com.eviive.personalapi.mapper;

import com.eviive.personalapi.dto.UserDto;
import com.eviive.personalapi.entity.User;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.ReportingPolicy.ERROR;

@Mapper(unmappedTargetPolicy = ERROR, componentModel = "spring", injectionStrategy = CONSTRUCTOR, uses = {RoleMapper.class})
public interface UserMapper {

    // to Entity

    User toEntity(UserDto userDto);

    List<User> toEntity(List<UserDto> userDtos);

    Set<User> toEntity(Set<UserDto> userDtos);

    // to DTO

    UserDto toDto(User user);

    List<UserDto> toDto(List<User> users);

    Set<UserDto> toDto(Set<User> users);

}
