package com.eviive.personalapi.mapper;

import com.eviive.personalapi.model.Skill;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.springframework.stereotype.Component;

@Component
public class SkillMapper extends ConfigurableMapper {
	
	protected void configure(MapperFactory mapperFactory) {
		mapperFactory.classMap(Skill.class, Skill.class)
					 .exclude("id")
					 .mapNulls(false)
					 .byDefault()
					 .register();
	}
	
}