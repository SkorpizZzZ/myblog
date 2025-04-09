package org.example.domain;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(exclude = "id")
public class Comment {
    private final Long id;
    private final String comment;
    private final Long postId;
}
