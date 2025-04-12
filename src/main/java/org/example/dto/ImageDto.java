package org.example.dto;

public record ImageDto(
        Long id,
        String path,
        Long postId
) {
}
