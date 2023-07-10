package com.eviive.personalapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "API_PROJECT")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, length = 512)
    private String description;

    @Column(nullable = false)
    private LocalDate creationDate;

    @Column(nullable = false)
    private String repoUrl;

    @Column(nullable = false)
    private String demoUrl;

    @Column(nullable = false)
    private Boolean featured;

    @Column(nullable = false)
    private Integer sort;

    @OneToOne(cascade = ALL, fetch = LAZY, orphanRemoval = true)
    @JoinColumn(name = "IMAGE_ID", referencedColumnName = "ID", foreignKey = @ForeignKey(name = "FK_PROJECT_IMAGE"))
    @ToString.Exclude
    private Image image;

    @ManyToMany(cascade = {DETACH, MERGE, PERSIST, REFRESH}, fetch = LAZY)
    @JoinTable(
            name = "API_PROJECT_SKILL_MAP",
            joinColumns = @JoinColumn(name = "PROJECT_ID", referencedColumnName = "ID", foreignKey = @ForeignKey(name = "FK_MAP_PROJECT")),
            inverseJoinColumns = @JoinColumn(name = "SKILL_ID", referencedColumnName = "ID", foreignKey = @ForeignKey(name = "FK_MAP_SKILL"))
    )
    @ToString.Exclude
    private Set<Skill> skills = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return Objects.equals(id, project.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
