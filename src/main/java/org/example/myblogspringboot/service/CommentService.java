package org.example.myblogspringboot.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.example.myblogspringboot.dto.CommentDto;
import org.example.myblogspringboot.mapper.CommentEntityMapper;
import org.example.myblogspringboot.repository.CommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentEntityMapper commentEntityMapper;

    public List<CommentDto> findByPostId(Long postId) {
        if (postId != null) {
            return commentRepository.findByPostId(postId).stream()
                    .map(commentEntityMapper::commentEntityToCommentDto)
                    .toList();
        } else {
            return Collections.emptyList();
        }
    }

    @Transactional
    public void deleteByPostId(Long postId) {
        if (postId != null) {
            commentRepository.deleteByPostId(postId);
        }
    }

    @Transactional
    public void addComment(Long postId, String comment) {
        if (StringUtils.isNotBlank(comment) && postId != null) {
            commentRepository.addComment(postId, comment);
        }
    }

    @Transactional
    public void updateComment(Long postId, String comment, Long commentId) {
        if (StringUtils.isNotBlank(comment)) {
            commentRepository.updateComment(postId, comment, commentId);
        }
    }

    @Transactional
    public void deleteComment(Long postId, Long commentId) {
            commentRepository.deleteComment(postId, commentId);
    }
}
