package com.eviive.personalapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "API_IMAGE")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private UUID uuid;

    @Column(nullable = false)
    private String altEn;

    @Column(nullable = false)
    private String altFr;

    @OneToOne(mappedBy = "image")
    @ToString.Exclude
    private Project project;

    @OneToOne(mappedBy = "image")
    @ToString.Exclude
    private Skill skill;

}
