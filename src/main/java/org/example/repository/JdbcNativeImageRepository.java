package org.example.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
    public void save(MultipartFile image, Long postId) {

        if (!image.isEmpty()) {
            String fileName = image.getOriginalFilename();
            String tomcatDir = System.getProperty("catalina.base");
            Path uploadDir = Paths.get(tomcatDir, "uploads", "images");
            Path filePath = uploadDir.resolve(fileName);

            try {
                if (!Files.exists(uploadDir)) {
                    Files.createDirectories(uploadDir);
                }
                Files.write(filePath, image.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            final String QUERY = """
                    INSERT INTO blog.images(path, post_id)
                    VALUES (?, ?)
                    """;
            String relativePath = "/uploads/images/" + fileName;
            jdbcTemplate.update(QUERY, relativePath, postId);
        }
    }
}
