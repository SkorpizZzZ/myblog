package org.example.repository;

import lombok.RequiredArgsConstructor;
import org.example.domain.Comment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcNativeCommentRepository implements CommentRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Comment> findByPostId(Long postId) {
        String query = """
                SELECT id, comment, post_id
                FROM blog.comments
                WHERE post_id = ?
                """;
        return jdbcTemplate.query(query, (rs, rowNum) -> new Comment(
                                rs.getLong("id"),
                                rs.getString("comment"),
                                rs.getLong("post_id")
                        ),
                postId
        );
    }
}
