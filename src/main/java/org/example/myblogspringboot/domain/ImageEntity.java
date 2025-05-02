package org.example.myblogspringboot.domain;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(exclude = "id")
public class ImageEntity {
    private final Long id;
    private final String path;
    private final Long postId;
}
