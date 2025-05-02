package org.example.myblogspringboot.dto;

public record CommentDto(
        Long id,
        String comment,
        Long postId
) {
}
