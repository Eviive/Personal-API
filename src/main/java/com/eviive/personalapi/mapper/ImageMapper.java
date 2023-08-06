package com.eviive.personalapi.mapper;

import com.eviive.personalapi.dto.ImageDTO;
import com.eviive.personalapi.entity.Image;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.ReportingPolicy.ERROR;

@Mapper(
        unmappedTargetPolicy = ERROR,
        componentModel = "spring",
        injectionStrategy = CONSTRUCTOR
)
public interface ImageMapper {

    // to Entity

    @Mapping(target = "project", ignore = true)
    @Mapping(target = "skill", ignore = true)
    Image toEntity(ImageDTO projectDTO);

    List<Image> toListEntity(Collection<ImageDTO> projectDTOs);

    Set<Image> toSetEntity(Collection<ImageDTO> projectDTOs);

    @AfterMapping
    default void afterMapping(@MappingTarget Image image) {
        if (image.getProject() != null) {
            image.getProject().setImage(image);
        }
        if (image.getSkill() != null) {
            image.getSkill().setImage(image);
        }
    }

    // to DTO

    ImageDTO toDTO(Image project);

    List<ImageDTO> toListDTO(Collection<Image> projects);

    Set<ImageDTO> toSetDTO(Collection<Image> projects);

}
