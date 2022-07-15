package com.eviive.personalapi.mapper;

import com.eviive.personalapi.model.Project;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapper extends ConfigurableMapper {
	
	protected void configure(MapperFactory mapperFactory) {
		mapperFactory.classMap(Project.class, Project.class)
					 .exclude("id")
					 .mapNulls(false)
					 .byDefault()
					 .register();
	}
	
}