package com.eviive.personalapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "API_Role")
@Table(
		name = "API_Role",
		uniqueConstraints = @UniqueConstraint(name = "UK_ROLE_NAME", columnNames = "name")
)
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = "users")
@NoArgsConstructor
@AllArgsConstructor
public class Role implements IModel {
	
	private static final String entityName = "role";
	
	@Id
	@SequenceGenerator(
			name = "api_role_sequence",
			sequenceName = "api_role_sequence",
			allocationSize = 1
	)
	@GeneratedValue(
			strategy = SEQUENCE,
			generator = "api_role_sequence"
	)
	@Column(name = "role_id")
	@Getter(onMethod = @__(@Override))
	@EqualsAndHashCode.Include
	private Long id;
	
	@Column(nullable = false)
	@Getter(onMethod = @__(@Override))
	@NotBlank
	private String name;
	
	@ManyToMany(mappedBy = "roles")
	@JsonIgnore
	private Set<ApiUser> users;
	
	@Override
	@JsonIgnore
	public String getEntityName() {
		return entityName;
	}
	
	@Override
	public void removeDependentElements() {
		for (ApiUser user: users) {
			user.removeRole(this);
		}
	}
	
}