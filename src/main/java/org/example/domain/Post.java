package org.example.domain;

import lombok.*;

import java.util.List;


@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(exclude = "id")
public class Post {
    private final Long id;
    private final String title;
    private final String textPreview;
    private final Long likesCount;
    private final String text;
    private List<String> comments;
    private List<String> tags;
}


