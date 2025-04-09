package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.domain.Post;
import org.example.repository.CommentRepository;
import org.example.repository.ImageRepository;
import org.example.repository.PostRepository;
import org.example.repository.TagRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final TagRepository tagRepository;
    private final ImageRepository imageRepository;

    public Page<Post> findAll(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);
        posts.forEach(post -> {
            post.setComments(commentRepository.findByPostId(post.getId()));
            post.setTags(tagRepository.findByPostId(post.getId()));
        });
        return posts;
    }

    public void save(
            String title,
            String text,
            String tags,
            MultipartFile image
    ) {
        Long savedPostId = postRepository.save(title, text);
        tagRepository.saveAll(tags, savedPostId);
        imageRepository.save(image, savedPostId);
    }
}
