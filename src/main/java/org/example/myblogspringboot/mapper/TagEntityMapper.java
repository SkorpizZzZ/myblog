package org.example.myblogspringboot.mapper;

import org.example.myblogspringboot.domain.TagEntity;
import org.example.myblogspringboot.dto.TagDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TagEntityMapper {
    TagEntity tagDtoToTagEntity(TagDto tagDto);
    TagDto tagEntityToTagDto(TagEntity tagDto);
}
