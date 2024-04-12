package com.eviive.personalapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
