package org.example.repository;

import lombok.RequiredArgsConstructor;
import org.example.domain.PostEntity;
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
    public Page<PostEntity> findAll(Pageable pageable) {
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
        List<PostEntity> posts = jdbcTemplate.query(QUERY, (rs, rowNum) -> new PostEntity(
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
    public Long save(String title, String text, String textPreview) {
        final String QUERY = """
                INSERT INTO blog.posts (title, text_preview, likes_count, text)
                VALUES (?, ? ,? ,?)
                RETURNING id
                """;
        return jdbcTemplate.queryForObject(QUERY, Long.class, title, textPreview, 0, text);
    }

    @Override
    public Long update(Long id, String title, String text, String textPreview) {
        final String QUERY = """
                UPDATE blog.posts
                SET title = ?, text = ?, text_preview = ?
                WHERE id = ?
                RETURNING id
                """;
        return jdbcTemplate.queryForObject(QUERY, Long.class, title, text, textPreview, id);
    }

    @Override
    public Optional<PostEntity> findById(Long id) {
        final String QUERY = """
                SELECT *
                FROM blog.posts
                WHERE id = ?
                """;
        return jdbcTemplate.query(
                QUERY,
                (rs, rowNum) -> new PostEntity(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("text_preview"),
                        rs.getLong("likes_count"),
                        rs.getString("text")
                ),
                id
        ).stream().findFirst();
    }

    @Override
    public void delete(Long id) {
        final String QUERY = """
                DELETE FROM blog.posts
                WHERE id = ?
                """;
        jdbcTemplate.update(QUERY, id);
    }

    @Override
    public void like(Long id) {
        final String QUERY = """
                UPDATE blog.posts
                SET likes_count = likes_count + 1
                WHERE id = ?
                """;
        jdbcTemplate.update(QUERY, id);

    }

    @Override
    public void dislike(Long id) {
        final String QUERY = """
                UPDATE blog.posts
                SET likes_count = likes_count - 1
                WHERE id = ?
                AND likes_count > 0
                """;
        jdbcTemplate.update(QUERY, id);

    }

    @Override
    public Page<PostEntity> findAllByTag(Pageable pageable, String tag) {
        final String COUNT_QUERY = """
                SELECT count(*)
                FROM blog.posts p 
                JOIN blog.tags t ON t.post_id = p.id
                WHERE t.tag = ?
                """;
        Integer countPosts = jdbcTemplate.queryForObject(COUNT_QUERY, Integer.class, tag);

        final String QUERY = """
                SELECT p.id, p.title, p.text_preview, p.likes_count, p.text
                FROM blog.posts p 
                JOIN blog.tags t ON t.post_id = p.id
                WHERE t.tag = ?
                ORDER BY p.id DESC LIMIT ? OFFSET ?
                """;
        List<PostEntity> posts = jdbcTemplate.query(QUERY, (rs, rowNum) -> new PostEntity(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("text_preview"),
                        rs.getLong("likes_count"),
                        rs.getString("text")
                ),
                tag,
                pageable.getPageSize(),
                pageable.getOffset()
        );
        return new PageImpl<>(posts, pageable, Optional.ofNullable(countPosts).orElse(0));
    }


}
