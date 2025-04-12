package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.domain.Comment;
import org.example.domain.Post;
import org.example.domain.Tag;
import org.example.repository.CommentRepository;
import org.example.repository.PostRepository;
import org.example.repository.TagRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final TagRepository tagRepository;
    private final ImageService imageService;

    public Page<Post> findAll(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);
        posts.forEach(post -> {
            post.setComments(commentRepository.findByPostId(post.getId()).stream()
                    .map(Comment::getComment)
                    .collect(Collectors.toList()));
            post.setTags(tagRepository.findByPostId(post.getId()).stream()
                    .map(Tag::getTag)
                    .collect(Collectors.toList()));
        });
        return posts;
    }

    public void save(
            String title,
            String text,
            String tags,
            MultipartFile image
    ) throws IOException {
        Long savedPostId = postRepository.save(title, text);
        tagRepository.saveAll(tags, savedPostId);
        imageService.saveImage(image, savedPostId);
    }
}
