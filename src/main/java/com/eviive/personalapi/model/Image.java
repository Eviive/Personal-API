package com.eviive.personalapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Image {
	
	@Column(name = "image_url", nullable = false)
	private String url;
	
	@Column(name = "image_description", nullable = false)
	private String description;
	
}