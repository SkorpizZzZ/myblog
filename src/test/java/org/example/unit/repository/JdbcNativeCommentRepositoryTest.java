package org.example.unit.repository;

import org.example.domain.CommentEntity;
import org.example.repository.JdbcNativeCommentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JdbcNativeCommentRepositoryTest {
    @InjectMocks
    private JdbcNativeCommentRepository repository;
    @Mock
    private JdbcTemplate jdbcTemplate;

    @Test
    void findByPostId() {
        //GIVEN
        Long postId = 1L;
        CommentEntity foundComment = new CommentEntity(
                1L,
                "Comment",
                1L
        );
        List<CommentEntity> expectedResult = Collections.singletonList(foundComment);
        //WHEN
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), anyLong())).thenReturn(expectedResult);
        //THEN
        List<CommentEntity> actualResult = repository.findByPostId(postId);
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    void deleteByPostId() {
        //GIVEN
        Long postId = 1L;
        //THEN
        repository.deleteByPostId(postId);
        verify(jdbcTemplate, times(1)).update(anyString(), anyLong());
    }

    @Test
    void addComment() {
        //GIVEN
        Long postId = 1L;
        String comment = "comment";
        //THEN
        repository.addComment(postId, comment);
        verify(jdbcTemplate, times(1)).update(anyString(), anyString(), anyLong());
    }

    @Test
    void updateComment() {
        //GIVEN
        Long postId = 1L;
        String comment = "comment";
        Long commentId = 1L;
        //THEN
        repository.updateComment(postId, comment, commentId);
        verify(jdbcTemplate, times(1)).update(anyString(), anyString(), anyLong(), anyLong());
    }

    @Test
    void deleteComment() {
        //GIVEN
        Long postId = 1L;
        Long commentId = 1L;
        //THEN
        repository.deleteComment(postId, commentId);
        verify(jdbcTemplate, times(1)).update(anyString(), anyLong(), anyLong());
    }
}