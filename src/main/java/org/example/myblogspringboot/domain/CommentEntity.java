package org.example.myblogspringboot.domain;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(exclude = "id")
public class CommentEntity {
    private final Long id;
    private final String comment;
    private final Long postId;
}
