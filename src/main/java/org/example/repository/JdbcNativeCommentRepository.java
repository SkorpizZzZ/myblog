package org.example.repository;

import lombok.RequiredArgsConstructor;
import org.example.domain.CommentEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcNativeCommentRepository implements CommentRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<CommentEntity> findByPostId(Long postId) {
        final String QUERY = """
                SELECT id, comment, post_id
                FROM blog.comments
                WHERE post_id = ?
                """;
        return jdbcTemplate.query(QUERY, (rs, rowNum) -> new CommentEntity(
                                rs.getLong("id"),
                                rs.getString("comment"),
                                rs.getLong("post_id")
                        ),
                postId
        );
    }

    @Override
    public void deleteByPostId(Long postId) {
        final String QUERY = """
                DELETE FROM blog.comments
                WHERE post_id = ?
                """;
        jdbcTemplate.update(QUERY, postId);
    }

    @Override
    public void addComment(Long postId, String comment) {
        final String QUERY = """
                INSERT INTO blog.comments(comment, post_id)
                VALUES (?, ?)
                """;
        jdbcTemplate.update(QUERY, comment, postId);
    }

    @Override
    public void updateComment(Long postId, String comment, Long commentId) {
        final String QUERY = """
                UPDATE blog.comments
                SET comment = ?
                WHERE id = ?
                AND comments.post_id = ?
                """;
        jdbcTemplate.update(QUERY, comment, commentId, postId);

    }

    @Override
    public void deleteComment(Long postId, Long commentId) {
        final String QUERY = """
                DELETE FROM blog.comments
                WHERE id = ?
                AND post_id = ?
                """;
        jdbcTemplate.update(QUERY, commentId, postId);

    }
}
