package org.example.myblogspringboot.repository;

import org.example.myblogspringboot.domain.TagEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository {
    List<TagEntity> findByPostId(Long postId);

    void saveAll(String tags, Long postId);

    void deleteByPostId(Long postId);

}
