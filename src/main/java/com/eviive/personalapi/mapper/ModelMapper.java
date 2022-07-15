package com.eviive.personalapi.mapper;

import com.eviive.personalapi.model.IModel;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

public class ModelMapper<E extends IModel> {
	
	private final MapperFactory mapperFactory = new DefaultMapperFactory.Builder()
																		.mapNulls(false)
																		.build();
	
	public ModelMapper(Class<E> eClass) {
		mapperFactory.classMap(eClass, eClass)
					 .exclude("id")
					 .byDefault()
					 .register();
	}
	
	public void map(E source, E destination) {
		mapperFactory.getMapperFacade().map(source, destination);
	}
	
}