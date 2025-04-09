package org.example.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

@Repository
@RequiredArgsConstructor
public class JdbcNativeImageRepository implements ImageRepository{

    private final JdbcTemplate jdbcTemplate;

    @Override
    public String findPathByPostId(Long postId) {
        String query = """
                SELECT path
                FROM blog.images
                WHERE post_id = ?
                """;
        return jdbcTemplate.queryForObject(query, String.class, postId);
    }

    @Override
    public void save(MultipartFile image, Long postId) {
        String query = """
                INSERT INTO blog.images(path, post_id)
                VALUES (?, ?)
                """;

        jdbcTemplate.update(query, image, postId)
    }
}
