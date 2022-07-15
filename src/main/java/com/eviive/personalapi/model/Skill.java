package com.eviive.personalapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "API_Skill")
@Table(
		name = "API_Skill",
		uniqueConstraints = @UniqueConstraint(name = "UK_SKILL_NAME", columnNames = "name")
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Skill implements IModel {
	
	@Id
	@SequenceGenerator(
			name = "api_skill_sequence",
			sequenceName = "api_skill_sequence",
			allocationSize = 1
	)
	@GeneratedValue(
			strategy = SEQUENCE,
			generator = "api_skill_sequence"
	)
	@Column(name = "skill_id")
	private Long id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(name = "logo_url", nullable = false)
	private String logoURL;
	
}