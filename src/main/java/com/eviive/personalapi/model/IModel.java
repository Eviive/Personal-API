package com.eviive.personalapi.model;

public interface IModel {
	
	String getEntityName();
	
	Long getId();
	
	String getName();
	
	default void removeDependentElements() {}
	
}