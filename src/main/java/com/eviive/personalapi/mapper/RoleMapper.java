package com.eviive.personalapi.mapper;

import com.eviive.personalapi.model.Role;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper extends ConfigurableMapper {
	
	protected void configure(MapperFactory mapperFactory) {
		mapperFactory.classMap(Role.class, Role.class)
					 .exclude("id")
					 .mapNulls(false)
					 .byDefault()
					 .register();
	}
	
}