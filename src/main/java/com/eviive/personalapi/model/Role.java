package com.eviive.personalapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "API_Role")
@Table(name = "API_Role")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
	
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
	@Column(name = "role_id", updatable = false)
	private Long id;
	
	private String name;
	
}