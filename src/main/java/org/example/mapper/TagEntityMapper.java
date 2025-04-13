package org.example.mapper;

import org.example.domain.TagEntity;
import org.example.dto.TagDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TagEntityMapper {
    TagEntity tagDtoToTagEntity(TagDto tagDto);
    TagDto tagEntityToTagDto(TagEntity tagDto);
}
