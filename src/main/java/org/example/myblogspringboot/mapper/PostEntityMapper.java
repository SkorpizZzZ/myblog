package org.example.myblogspringboot.mapper;

import org.example.myblogspringboot.domain.PostEntity;
import org.example.myblogspringboot.dto.PostDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostEntityMapper {
    PostEntity postDtoToPostEntity(PostDto postDto);

    PostDto postEntityToPostDto(PostEntity postDto);
}
