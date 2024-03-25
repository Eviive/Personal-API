package com.eviive.personalapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
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

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private Integer sort;

    @OneToOne(cascade = ALL, fetch = LAZY, orphanRemoval = true)
    @JoinColumn(name = "IMAGE_ID")
    @ToString.Exclude
    private Image image;

    @ManyToMany(mappedBy = "skills", fetch = LAZY)
    @ToString.Exclude
    private Set<Project> projects = new HashSet<>();

}
