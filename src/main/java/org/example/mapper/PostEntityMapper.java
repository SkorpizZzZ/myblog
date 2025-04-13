package org.example.mapper;

import org.example.domain.PostEntity;
import org.example.dto.PostDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostEntityMapper {
    PostEntity postDtoToPostEntity(PostDto postDto);

    PostDto postEntityToPostDto(PostEntity postDto);
}
