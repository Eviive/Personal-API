package com.eviive.personalapi.entity;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static com.eviive.personalapi.entity.Authority.READ_ACTUATOR;
import static com.eviive.personalapi.entity.Authority.READ_PROJECT;
import static com.eviive.personalapi.entity.Authority.READ_SKILL;
import static com.eviive.personalapi.entity.Authority.WRITE_PROJECT;
import static com.eviive.personalapi.entity.Authority.WRITE_SKILL;

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
            WRITE_PROJECT,
            WRITE_SKILL,
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
                authorities.stream().map(Authority::getAuthority)
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
