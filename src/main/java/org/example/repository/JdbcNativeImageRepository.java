package org.example.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcNativeImageRepository implements ImageRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public String findPathByPostId(Long postId) {
        final String QUERY = """
                SELECT path
                FROM blog.images
                WHERE post_id = ?
                """;
        return jdbcTemplate.queryForObject(QUERY, String.class, postId);
    }

    @Override
    public void save(String fileName, Long postId) {
        final String QUERY = """
                INSERT INTO blog.images(path, post_id)
                VALUES (?, ?)
                """;
        String relativePath = "/uploads/images/" + fileName;
        jdbcTemplate.update(QUERY, relativePath, postId);
    }
}
