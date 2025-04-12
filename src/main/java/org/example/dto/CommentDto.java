package org.example.dto;

public record CommentDto(
        Long id,
        String comment,
        Long postId
) {
}
