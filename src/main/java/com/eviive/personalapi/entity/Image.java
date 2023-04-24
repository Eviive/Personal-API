package com.eviive.personalapi.entity;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "API_IMAGE")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Image implements IEntity {

    @Id
    @SequenceGenerator(name = "API_IMAGE_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = SEQUENCE, generator = "API_IMAGE_SEQUENCE")
    private Long id;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private String alt;

}
