package org.example.repository;

public interface ImageRepository {

    String findPathByPostId(Long postId);

    void save(String fileName, Long savedPostId);
}
