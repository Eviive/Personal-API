package com.eviive.personalapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "API_Skill")
@Table(
		name = "API_Skill",
		uniqueConstraints = @UniqueConstraint(name = "UK_SKILL_NAME", columnNames = "name")
)
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = "projects")
@NoArgsConstructor
@AllArgsConstructor
public class Skill implements IModel {
	
	private static final String entityName = "skill";
	
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
	@Getter(onMethod = @__(@Override))
	@EqualsAndHashCode.Include
	private Long id;
	
	@Column(nullable = false)
	@Getter(onMethod = @__(@Override))
	@NotBlank
	private String name;
	
	@Column(name = "logo_url", nullable = false)
	@NotBlank
	private String logoURL;
	
	@ManyToMany(mappedBy = "skills")
	@JsonIgnore
	private Set<Project> projects;
	
	@Override
	@JsonIgnore
	public String getEntityName() {
		return entityName;
	}
	
	@Override
	public void removeDependentElements() {
		for (Project project: projects) {
			project.removeSkill(this);
		}
	}
}