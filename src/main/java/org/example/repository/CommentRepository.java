package org.example.repository;

import org.example.domain.CommentEntity;

import java.util.List;

public interface CommentRepository {
    List<CommentEntity> findByPostId(Long postId);

    void deleteByPostId(Long postId);

    void addComment(Long postId, String comment);

    void updateComment(Long postId, String comment, Long commentId);

    void deleteComment(Long postId, Long commentId);
}
