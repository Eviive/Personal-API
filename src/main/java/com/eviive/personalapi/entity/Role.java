package com.eviive.personalapi.entity;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static com.eviive.personalapi.entity.Authority.CREATE_PROJECTS;
import static com.eviive.personalapi.entity.Authority.CREATE_SKILLS;
import static com.eviive.personalapi.entity.Authority.DELETE_PROJECTS;
import static com.eviive.personalapi.entity.Authority.DELETE_SKILLS;
import static com.eviive.personalapi.entity.Authority.READ_ACTUATOR;
import static com.eviive.personalapi.entity.Authority.READ_PROJECTS;
import static com.eviive.personalapi.entity.Authority.READ_SKILLS;
import static com.eviive.personalapi.entity.Authority.REVALIDATE_PORTFOLIO;
import static com.eviive.personalapi.entity.Authority.UPDATE_PROJECTS;
import static com.eviive.personalapi.entity.Authority.UPDATE_SKILLS;

@RequiredArgsConstructor
public enum Role {

    ANONYMOUS(
        Set.of(
            READ_PROJECTS,
            READ_SKILLS
        ),
        null
    ),
    ADMIN(
        Set.of(
            CREATE_PROJECTS, UPDATE_PROJECTS, DELETE_PROJECTS,
            CREATE_SKILLS, UPDATE_SKILLS, DELETE_SKILLS,
            REVALIDATE_PORTFOLIO,
            READ_ACTUATOR
        ),
        Set.of(
            ANONYMOUS
        )
    );

    private final Set<Authority> authorities;

    @Nullable
    @Getter
    private final Set<Role> subRoles;

    public List<GrantedAuthority> getAuthorities() {
        final Stream<GrantedAuthority> grantedAuthorities = Stream
            .concat(
                Stream.of(toString()),
                authorities.stream().map(Authority::getName)
            )
            .map(SimpleGrantedAuthority::new)
            .map(GrantedAuthority.class::cast);

        if (subRoles == null) {
            return grantedAuthorities.toList();
        }

        return Stream
            .concat(
                grantedAuthorities,
                subRoles.stream().flatMap(sR -> sR.getAuthorities().stream())
            )
            .toList();
    }

    @Override
    public String toString() {
        return "ROLE_" + name();
    }

}
