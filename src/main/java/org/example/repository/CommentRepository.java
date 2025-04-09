package org.example.repository;

import org.example.domain.Comment;

import java.util.List;

public interface CommentRepository {
    List<Comment> findByPostId(Long postId);
}
