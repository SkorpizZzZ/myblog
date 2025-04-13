package org.example.mapper;

import org.example.domain.ImageEntity;
import org.example.dto.ImageDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ImageEntityMapper {
    ImageEntity imageDtoToImageEntity(ImageDto imageDto);
    ImageDto imageEntityToImageDto(ImageEntity imageDto);
}
