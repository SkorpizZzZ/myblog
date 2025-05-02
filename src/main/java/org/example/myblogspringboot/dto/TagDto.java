package org.example.myblogspringboot.dto;

public record TagDto(
        Long id,
        String tag,
        Long postId
) {
}
