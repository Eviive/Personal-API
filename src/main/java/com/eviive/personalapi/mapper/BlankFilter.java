package com.eviive.personalapi.mapper;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.NullFilter;
import ma.glasnost.orika.metadata.Type;

public class BlankFilter<A, B> extends NullFilter<A, B> {
	
	@Override
	public <S extends A, D extends B> boolean shouldMap(Type<S> sourceType, String sourceName, S source, Type<D> destType, String destName, D dest, MappingContext mappingContext) {
		return !"".equals(source);
	}
	
}