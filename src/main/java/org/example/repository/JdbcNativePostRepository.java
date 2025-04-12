package org.example.repository;

import lombok.RequiredArgsConstructor;
import org.example.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcNativePostRepository implements PostRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Page<Post> findAll(Pageable pageable) {
        final String COUNT_QUERY = """
                SELECT count(*)
                FROM blog.posts
                """;
        Integer countPosts = jdbcTemplate.queryForObject(COUNT_QUERY, Integer.class);

       final String QUERY = """
                SELECT id, title, text_preview, likes_count, text
                FROM blog.posts
                ORDER BY id DESC LIMIT ? OFFSET ?
                """;
        List<Post> posts = jdbcTemplate.query(QUERY, (rs, rowNum) -> new Post(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("text_preview"),
                        rs.getLong("likes_count"),
                        rs.getString("text")
                ),
                pageable.getPageSize(),
                pageable.getOffset()
        );
        return new PageImpl<>(posts, pageable, Optional.ofNullable(countPosts).orElse(0));
    }

    @Override
    public Long save(String title, String text) {
        final String QUERY = """
                INSERT INTO blog.posts (title, text_preview, likes_count, text)
                VALUES (?, ? ,? ,?)
                RETURNING id
                """;
        return jdbcTemplate.queryForObject(QUERY, Long.class, title, makeTextPreview(text), 0, text);
    }

    private String makeTextPreview(String text) {
        return text.length() < 50 ? text : text.substring(0, 50).concat("...");
    }
}
