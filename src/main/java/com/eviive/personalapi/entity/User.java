package com.eviive.personalapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "API_USER")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User implements IEntity {

    @Id
    @SequenceGenerator(name = "API_USER_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = SEQUENCE, generator = "API_USER_SEQUENCE")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String password;

    @ManyToMany(cascade = ALL, fetch = LAZY)
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
