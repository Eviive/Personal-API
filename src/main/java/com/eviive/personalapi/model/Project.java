package com.eviive.personalapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.SEQUENCE;
import static javax.persistence.InheritanceType.SINGLE_TABLE;

@Entity(name = "API_Project")
@Table(
		name = "API_Project",
		uniqueConstraints = @UniqueConstraint(name = "UK_PROJECT_NAME", columnNames = "name")
)
@Inheritance(strategy = SINGLE_TABLE)
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = "skills")
@NoArgsConstructor
@AllArgsConstructor
public class Project implements IModel {
	
	private static final String entityName = "project";
	
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
	@Getter(onMethod = @__(@Override))
	@EqualsAndHashCode.Include
	private Long id;
	
	@Column(nullable = false)
	@Getter(onMethod = @__(@Override))
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
	
	@ManyToMany
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
	private Set<Skill> skills;
	
	@Embedded
	@Valid
	@NotNull
	private Image image;
	
	@Column(nullable = false)
	@NotNull
	private Boolean featured;
	
	@Override
	@JsonIgnore
	public String getEntityName() {
		return entityName;
	}
	
	public void addSkill(Skill skill) {
		if (skills == null) {
			skills = new HashSet<>();
		}
		skills.add(skill);
	}
	
	public void removeSkill(Skill skill) {
		if (skills != null)
			skills.remove(skill);
	}
	
}