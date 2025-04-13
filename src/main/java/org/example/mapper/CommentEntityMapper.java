package org.example.mapper;

import org.example.domain.CommentEntity;
import org.example.dto.CommentDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentEntityMapper {

    CommentEntity commentDtoToCommentEntity(CommentDto commentDto);
    CommentDto commentEntityToCommentDto(CommentEntity commentDto);
}
