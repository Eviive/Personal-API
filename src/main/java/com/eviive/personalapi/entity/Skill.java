package com.eviive.personalapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.Set;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "API_SKILL")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Skill implements IEntity {

    @Id
    @SequenceGenerator(name = "API_SKILL_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = SEQUENCE, generator = "API_SKILL_SEQUENCE")
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToOne(cascade = ALL, fetch = LAZY, orphanRemoval = true)
    @JoinColumn(name = "IMAGE_ID", referencedColumnName = "ID", foreignKey = @ForeignKey(name = "FK_SKILL_IMAGE"))
    @ToString.Exclude
    private Image image;

    @ManyToMany(mappedBy = "skills")
    @ToString.Exclude
    private Set<Project> projects;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Skill skill = (Skill) o;
        return Objects.equals(id, skill.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
