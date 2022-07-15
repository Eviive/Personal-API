package com.eviive.personalapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "API_User")
@Table(
		name = "API_User",
		uniqueConstraints = @UniqueConstraint(name = "UK_USER_USERNAME", columnNames = "username")
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiUser implements IModel {
	
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
	private Long id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private String username;
	
	@Column(nullable = false)
	private String password;
	
	@ManyToMany(fetch = EAGER)
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
	private List<Role> roles;
	
	public boolean addRole(Role role) {
		if (roles == null) {
			roles = new ArrayList<>();
		}
		return roles.add(role);
	}
	
}