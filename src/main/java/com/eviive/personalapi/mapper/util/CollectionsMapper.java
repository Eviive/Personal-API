package com.eviive.personalapi.mapper.util;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface CollectionsMapper<E, D> {

    // to Entity

    List<E> toListEntity(Collection<D> DTOs);

    Set<E> toSetEntity(Collection<D> DTOs);

    // to DTO

    List<D> toListDTO(Collection<E> entities);

    Set<D> toSetDTO(Collection<E> entities);

}
