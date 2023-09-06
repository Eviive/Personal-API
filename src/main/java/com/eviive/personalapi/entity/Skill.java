package com.eviive.personalapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "API_SKILL")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Skill {

    public static final String AZURE_CONTAINER_NAME = "skill-images";

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private Integer sort;

    @OneToOne(cascade = ALL, fetch = LAZY, orphanRemoval = true)
    @JoinColumn(name = "IMAGE_ID", referencedColumnName = "ID", foreignKey = @ForeignKey(name = "FK_SKILL_IMAGE"))
    @ToString.Exclude
    private Image image;

    @ManyToMany(mappedBy = "skills", fetch = LAZY)
    @ToString.Exclude
    private Set<Project> projects = new HashSet<>();

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
