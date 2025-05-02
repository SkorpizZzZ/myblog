package org.example.myblogspringboot.mapper;

import org.example.myblogspringboot.domain.ImageEntity;
import org.example.myblogspringboot.dto.ImageDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ImageEntityMapper {
    ImageEntity imageDtoToImageEntity(ImageDto imageDto);
    ImageDto imageEntityToImageDto(ImageEntity imageDto);
}
