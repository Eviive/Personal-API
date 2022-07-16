package com.eviive.personalapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Image {
	
	@Column(name = "image_url", nullable = false)
	@NotBlank
	private String url;
	
	@Column(name = "image_description", nullable = false)
	@NotBlank
	private String description;
	
}