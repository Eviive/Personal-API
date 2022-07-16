package com.eviive.personalapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "API_User")
@Table(
		name = "API_User",
		uniqueConstraints = @UniqueConstraint(name = "UK_USER_USERNAME", columnNames = "username")
)
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = "roles")
@NoArgsConstructor
@AllArgsConstructor
public class ApiUser implements IModel {
	
	private static final String entityName = "user";
	
	@Id
	@SequenceGenerator(
			name = "api_user_sequence",
			sequenceName = "api_user_sequence",
			allocationSize = 1
	)
	@GeneratedValue(
			strategy = SEQUENCE,
			generator = "api_user_sequence"
	)
	@Column(name = "user_id")
	@Getter(onMethod = @__(@Override))
	@EqualsAndHashCode.Include
	private Long id;
	
	@Column(nullable = false)
	@Getter(onMethod = @__(@Override))
	@NotBlank
	private String name;
	
	@Column(nullable = false)
	@NotBlank
	private String username;
	
	@Column(nullable = false)
	@NotBlank
	private String password;
	
	@ManyToMany
	@JoinTable(
			name = "API_User_Role_Map",
			joinColumns = @JoinColumn(
					name = "user_id",
					referencedColumnName = "user_id",
					foreignKey = @ForeignKey(name = "FK_Map_User")
			),
			inverseJoinColumns = @JoinColumn(
					name = "role_id",
					referencedColumnName = "role_id",
					foreignKey = @ForeignKey(name = "FK_Map_Role")
			)
	)
	@NotNull
	private Set<Role> roles;
	
	@Override
	@JsonIgnore
	public String getEntityName() {
		return entityName;
	}
	
	public void addRole(Role role) {
		if (roles == null) {
			roles = new HashSet<>();
		}
		roles.add(role);
	}
	
	public void removeRole(Role role) {
		if (roles != null)
			roles.remove(role);
	}
	
}