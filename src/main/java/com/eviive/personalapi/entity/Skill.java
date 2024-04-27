package com.eviive.personalapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "API_SKILL")
@NamedEntityGraph(name = "skill-image", attributeNodes = @NamedAttributeNode("image"))
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

    @OneToOne(cascade = ALL, mappedBy = "skill", orphanRemoval = true)
    @ToString.Exclude
    private Image image;

    @ManyToMany(mappedBy = "skills", fetch = LAZY)
    @ToString.Exclude
    private Set<Project> projects = new HashSet<>();

}
