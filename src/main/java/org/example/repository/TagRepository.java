package org.example.repository;

import org.example.domain.Tag;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository {
    List<Tag> findByPostId(Long postId);

    void saveAll(String tags, Long savedPostId);
}
