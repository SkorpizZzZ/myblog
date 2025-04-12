package org.example.dto;

public record TagDto(
        Long id,
        String tag,
        Long postId
) {
}
