package org.example.repository;

import org.springframework.web.multipart.MultipartFile;

public interface ImageRepository {

    String findPathByPostId(Long postId);

    void save(MultipartFile image, Long savedPostId);
}
