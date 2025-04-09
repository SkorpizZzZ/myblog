package org.example.repository;

import lombok.RequiredArgsConstructor;
import org.example.domain.Tag;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcNativeTagRepository implements TagRepository{

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Tag> findByPostId(Long postId) {
        String query = """
                SELECT id, tag, post_id
                FROM blog.tags
                WHERE post_id = ?
                """;
        return jdbcTemplate.query(query, (rs, rowNum) -> new Tag(
                        rs.getLong("id"),
                        rs.getString("tag"),
                        rs.getLong("post_id")
                ),
                postId
        );
    }

    @Override
    public void saveAll(String tags, Long postId) {
        String[] inputTags = tags.split(" ");
        String query = """
                INSERT INTO blog.tags(tag, post_id)
                VALUES (?, ?)
                """;
        jdbcTemplate.batchUpdate(query,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, inputTags[i]);
                        ps.setLong(2, postId);
                    }
                    @Override
                    public int getBatchSize() {
                        return 50;
                    }
                });
    }
}
