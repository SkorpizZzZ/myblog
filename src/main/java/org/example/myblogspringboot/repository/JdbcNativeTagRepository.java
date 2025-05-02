package org.example.myblogspringboot.repository;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.example.myblogspringboot.domain.TagEntity;
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
    public List<TagEntity> findByPostId(Long postId) {
       final String QUERY = """
                SELECT id, tag, post_id
                FROM blog.tags
                WHERE post_id = ?
                """;
        return jdbcTemplate.query(QUERY, (rs, rowNum) -> new TagEntity(
                        rs.getLong("id"),
                        rs.getString("tag"),
                        rs.getLong("post_id")
                ),
                postId
        );
    }

    @Override
    public void saveAll(String tags, Long postId) {
        if (StringUtils.isBlank(tags)) {
            return;
        }

        String[] inputTags = tags.split(" ");
        final String QUERY = """
                INSERT INTO blog.tags(tag, post_id)
                VALUES (?, ?)
                """;
        jdbcTemplate.batchUpdate(QUERY,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, inputTags[i]);
                        ps.setLong(2, postId);
                    }
                    @Override
                    public int getBatchSize() {
                        return inputTags.length;
                    }
                });
    }

    @Override
    public void deleteByPostId(Long postId) {
        final String QUERY = """
                DELETE FROM blog.tags
                WHERE post_id = ?
                """;
        jdbcTemplate.update(QUERY, postId);
    }
}
