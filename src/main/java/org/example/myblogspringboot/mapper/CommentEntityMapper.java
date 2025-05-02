package org.example.myblogspringboot.mapper;

import org.example.myblogspringboot.domain.CommentEntity;
import org.example.myblogspringboot.dto.CommentDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentEntityMapper {

    CommentEntity commentDtoToCommentEntity(CommentDto commentDto);
    CommentDto commentEntityToCommentDto(CommentEntity commentDto);
}
