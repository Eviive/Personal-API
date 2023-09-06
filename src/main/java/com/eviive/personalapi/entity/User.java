package com.eviive.personalapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "API_USER")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false)
    private String password;

    @ManyToMany
    @JoinTable(
            name = "API_USER_ROLE_MAP",
            joinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "ID", foreignKey = @ForeignKey(name = "FK_MAP_USER")),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID", foreignKey = @ForeignKey(name = "FK_MAP_ROLE"))
    )
    @ToString.Exclude
    private Set<Role> roles = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
