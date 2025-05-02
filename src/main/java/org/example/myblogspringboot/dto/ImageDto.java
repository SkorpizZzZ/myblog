package org.example.myblogspringboot.dto;

public record ImageDto(
        Long id,
        String path,
        Long postId
) {
}
