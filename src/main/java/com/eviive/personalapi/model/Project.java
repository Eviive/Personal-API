package com.eviive.personalapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.SEQUENCE;
import static javax.persistence.InheritanceType.SINGLE_TABLE;

@Entity(name = "API_Project")
@Table(
		name = "API_Project",
		uniqueConstraints = @UniqueConstraint(name = "UK_PROJECT_NAME", columnNames = "name")
)
@Inheritance(strategy = SINGLE_TABLE)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project implements IModel {
	
	@Id
	@SequenceGenerator(
			name = "api_project_sequence",
			sequenceName = "api_project_sequence",
			allocationSize = 1
	)
	@GeneratedValue(
			strategy = SEQUENCE,
			generator = "api_project_sequence"
	)
	@Column(name = "project_id")
	private Long id;
	
	@Column(nullable = false)
	@NotBlank
	private String name;
	
	@Column(nullable = false)
	@NotBlank
	private String description;
	
	@Column(name = "repo_url", nullable = false)
	@NotBlank
	private String repoURL;
	
	@Column(name = "demo_url", nullable = false)
	@NotBlank
	private String demoURL;
	
	@ManyToMany(fetch = EAGER)
	@JoinTable(
			name = "API_Project_Skill_Map",
			joinColumns = @JoinColumn(
					name = "project_id",
					referencedColumnName = "project_id",
					foreignKey = @ForeignKey(name = "FK_Map_Project")
			),
			inverseJoinColumns = @JoinColumn(
					name = "skill_id",
					referencedColumnName = "skill_id",
					foreignKey = @ForeignKey(name = "FK_Map_Skill")
			)
	)
	@NotNull
	private List<Skill> skills;
	
	@Embedded
	@Valid
	@NotNull
	private Image image;
	
	@Column(nullable = false)
	@NotNull
	private Boolean featured;
	
	public boolean addSkill(Skill skill) {
		if (skills == null) {
			skills = new ArrayList<>();
		}
		return skills.add(skill);
	}
	
}