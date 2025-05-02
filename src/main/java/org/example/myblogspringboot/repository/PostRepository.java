package org.example.myblogspringboot.repository;

import org.example.myblogspringboot.domain.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PostRepository {
    Page<PostEntity> findAll(Pageable pageable);

    Long save(String title, String text, String textPreview);

    Long update(Long id, String title, String text, String textPreview);

    Optional<PostEntity> findById(Long id);

    void delete(Long id);

    void like(Long postId);

    void dislike(Long postId);

    Page<PostEntity> findAllByTag(Pageable pageable, String tag);
}
