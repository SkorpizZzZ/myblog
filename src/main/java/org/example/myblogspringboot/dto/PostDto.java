package org.example.myblogspringboot.dto;

import java.util.List;
import java.util.stream.Collectors;

public record PostDto(
        Long id,
        String title,
        String textPreview,
        Long likesCount,
        String text,
        List<CommentDto> comments,
        List<TagDto> tags
) {
   public String getTagsAsText() {
     return tags.stream()
             .map(TagDto::tag)
             .collect(Collectors.joining(" "));
    }
}
