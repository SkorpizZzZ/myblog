package org.example.myblogspringboot.repository;

import org.example.myblogspringboot.domain.CommentEntity;

import java.util.List;

public interface CommentRepository {
    List<CommentEntity> findByPostId(Long postId);

    void deleteByPostId(Long postId);

    void addComment(Long postId, String comment);

    void updateComment(Long postId, String comment, Long commentId);

    void deleteComment(Long postId, Long commentId);
}
