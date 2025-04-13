package org.example.domain;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(exclude = "id")
public class TagEntity {
    private final Long id;
    private final String tag;
    private final Long postId;
}
