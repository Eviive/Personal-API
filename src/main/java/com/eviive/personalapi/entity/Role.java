package com.eviive.personalapi.entity;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static com.eviive.personalapi.entity.Scope.CREATE_PROJECT;
import static com.eviive.personalapi.entity.Scope.CREATE_SKILL;
import static com.eviive.personalapi.entity.Scope.DELETE_PROJECT;
import static com.eviive.personalapi.entity.Scope.DELETE_SKILL;
import static com.eviive.personalapi.entity.Scope.READ_ACTUATOR;
import static com.eviive.personalapi.entity.Scope.READ_PROJECT;
import static com.eviive.personalapi.entity.Scope.READ_SKILL;
import static com.eviive.personalapi.entity.Scope.REVALIDATE_PORTFOLIO;
import static com.eviive.personalapi.entity.Scope.UPDATE_PROJECT;
import static com.eviive.personalapi.entity.Scope.UPDATE_SKILL;

@RequiredArgsConstructor
public enum Role {

    ANONYMOUS(
        Set.of(
            READ_PROJECT,
            READ_SKILL
        ),
        null
    ),
    ADMIN(
        Set.of(
            CREATE_PROJECT, UPDATE_PROJECT, DELETE_PROJECT,
            CREATE_SKILL, UPDATE_SKILL, DELETE_SKILL,
            REVALIDATE_PORTFOLIO,
            READ_ACTUATOR
        ),
        Set.of(
            ANONYMOUS
        )
    );

    private final Set<Scope> scopes;

    @Nullable
    @Getter
    private final Set<Role> subRoles;

    public List<GrantedAuthority> getAuthorities() {
        final Stream<GrantedAuthority> grantedAuthorities = Stream
            .concat(
                Stream.of(toString()),
                scopes.stream().map(Scope::getName)
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
