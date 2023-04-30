package com.eviive.personalapi.mapper;

import com.eviive.personalapi.dto.ImageDTO;
import com.eviive.personalapi.entity.Image;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.ReportingPolicy.ERROR;

@Mapper(unmappedTargetPolicy = ERROR, componentModel = "spring", injectionStrategy = CONSTRUCTOR)
public interface ImageMapper {

    // to Entity

    @Mapping(target = "uri", ignore = true)
    Image toEntity(ImageDTO projectDTO);

    List<Image> toListEntity(Collection<ImageDTO> projectDTOs);

    Set<Image> toSetEntity(Collection<ImageDTO> projectDTOs);

    // to DTO

    ImageDTO toDTO(Image project);

    List<ImageDTO> toListDTO(Collection<Image> projects);

    Set<ImageDTO> toSetDTO(Collection<Image> projects);

}
