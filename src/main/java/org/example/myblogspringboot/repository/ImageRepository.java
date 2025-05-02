package org.example.myblogspringboot.repository;

import java.util.Optional;

public interface ImageRepository {

    Optional<String> findPathByPostId(Long postId);

    void save(String fileName, Long savedPostId);

    void deleteByPostId(Long id);
}
