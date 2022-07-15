package com.eviive.personalapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "API_Role")
@Table(
		name = "API_Role",
		uniqueConstraints = @UniqueConstraint(name = "UK_ROLE_NAME", columnNames = "name")
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role implements IModel {
	
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
	private Long id;
	
	@Column(nullable = false)
	private String name;
	
}