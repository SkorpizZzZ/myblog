package org.example.repository;

import org.example.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepository {
    Page<Post> findAll(Pageable pageable);

    Long save(String title, String text);
}
