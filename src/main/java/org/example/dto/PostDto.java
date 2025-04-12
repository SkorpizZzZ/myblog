package org.example.dto;

import org.example.domain.Comment;
import org.example.domain.Tag;

import java.util.List;

public record PostDto(
        Long id,
        String title,
        String textPreview,
        Long likesCount,
        String text,
        List<Comment> comments,
        List<Tag> tags
) {
}
