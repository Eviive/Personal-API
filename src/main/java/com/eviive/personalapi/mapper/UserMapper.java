package com.eviive.personalapi.mapper;

import com.eviive.personalapi.dto.CurrentUserDTO;
import com.eviive.personalapi.entity.User;
import jakarta.annotation.Nullable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.ReportingPolicy.ERROR;

@Mapper(
    unmappedTargetPolicy = ERROR,
    componentModel = SPRING,
    injectionStrategy = CONSTRUCTOR
)
public interface UserMapper {

    // to Current DTO

    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "username", expression = "java( user != null ? user.getUsername() : \"Guest\" )")
    @Mapping(target = "authorities", source = "authorities")
    CurrentUserDTO toCurrentDTO(@Nullable User user, Set<String> authorities);

    default CurrentUserDTO toCurrentDTO(@Nullable User user, Collection<? extends GrantedAuthority> authorities) {
        return toCurrentDTO(
            user,
            authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet())
        );
    }

}
