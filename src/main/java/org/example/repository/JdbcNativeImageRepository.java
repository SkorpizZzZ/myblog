package org.example.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcNativeImageRepository implements ImageRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<String> findPathByPostId(Long postId) {
        final String QUERY = """
                SELECT path
                FROM blog.images
                WHERE post_id = ?
                """;
        return jdbcTemplate.query(QUERY, (rs, rowNum) -> rs.getString("path"), postId).stream()
                .findFirst();
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

    @Override
    public void deleteByPostId(Long postId) {
        final String QUERY = """
                DELETE FROM blog.images
                WHERE post_id = ?
                """;
        jdbcTemplate.update(QUERY, postId);
    }
}
