package org.example.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.example.domain.PostEntity;
import org.example.dto.PostDto;
import org.example.mapper.CommentEntityMapper;
import org.example.mapper.PostEntityMapper;
import org.example.mapper.TagEntityMapper;
import org.example.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.text.MessageFormat;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    private final ImageService imageService;
    private final TagService tagService;
    private final CommentService commentService;

    private final PostEntityMapper postEntityMapper;
    private final CommentEntityMapper commentEntityMapper;
    private final TagEntityMapper tagEntityMapper;


    public Page<PostDto> findAll(Pageable pageable, String tag) {
        if (StringUtils.isNotBlank(tag)) {
         return postRepository.findAllByTag(pageable, tag).map(post -> collectPostAttributes(post, post.getId()));
        } else {
            return postRepository.findAll(pageable).map(post -> collectPostAttributes(post, post.getId()));
        }
    }

    private PostDto collectPostAttributes(PostEntity post, Long postId) {
        post.setComments(commentService.findByPostId(postId).stream()
                .map(commentEntityMapper::commentDtoToCommentEntity)
                .toList());
        post.setTags(tagService.findByPostId(
                        postId).stream()
                .map(tagEntityMapper::tagDtoToTagEntity)
                .toList()
        );
        return postEntityMapper.postEntityToPostDto(post);
    }

    @Transactional
    public void save(
            String title,
            String text,
            String tags,
            MultipartFile image
    ) {
        Long savedPostId = postRepository.save(title, text, makeTextPreview(text));
        tagService.saveAll(tags, savedPostId);
        imageService.saveImage(image, savedPostId);
    }

    public PostDto findById(Long id) {
        if (id == null) {
            throw new RuntimeException("Не передан обязательный параметр - идентификатор Поста");
        }
        PostEntity post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        MessageFormat.format("Post с идентификатором {0} не найден", id)
                ));
        return collectPostAttributes(post, id);
    }

    private String makeTextPreview(String text) {
        if (StringUtils.isNotBlank(text)) {
            return text.length() < 50 ? text : text.substring(0, 50).concat("...");
        } else {
            return "";
        }
    }

    @Transactional
    public void delete(Long postId) {
        if (postId == null) {
            throw new RuntimeException("Не передан обязательный параметр - идентификатор Поста");
        }
        tagService.deleteByPostId(postId);
        imageService.deleteByPostId(postId);
        commentService.deleteByPostId(postId);
        postRepository.delete(postId);
    }

    @Transactional
    public void update(Long id, String title, String text, String tags, MultipartFile image) {
        Long updatePostId = postRepository.update(id, title, text, makeTextPreview(text));
        tagService.updateAll(tags, updatePostId);
        imageService.updateImage(image, updatePostId);
    }

    @Transactional
    public void like(Long postId, boolean likeValue) {
        if (postId != null) {
            if (likeValue) {
                postRepository.like(postId);
            } else {
                postRepository.dislike(postId);
            }
        }
    }

    @Transactional
    public void addComment(Long id, String comment) {
        if (StringUtils.isNotBlank(comment) && id != null) {
            commentService.addComment(id, comment);
        }
    }

    @Transactional
    public void updateComment(Long postId, String comment, Long commentId) {
        if (StringUtils.isNotBlank(comment)) {
            commentService.updateComment(postId, comment, commentId);
        }
    }

    @Transactional
    public void deleteComment(Long postId, Long commentId) {
        commentService.deleteComment(postId, commentId);
    }
}
