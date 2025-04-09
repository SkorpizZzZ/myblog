package org.example.domain;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(exclude = "id")
public class Image {
    private final Long id;
    private final String path;
    private final Long postId;
}
