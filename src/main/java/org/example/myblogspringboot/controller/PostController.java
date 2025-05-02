package org.example.myblogspringboot.controller;

import lombok.RequiredArgsConstructor;
import org.example.myblogspringboot.dto.PostDto;
import org.example.myblogspringboot.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    @GetMapping
    public String posts(
            @RequestParam(name = "pageNumber", defaultValue = "0") int page,
            @RequestParam(name = "pageSize", defaultValue = "5") int pageSize,
            @RequestParam(name = "search", required = false) String tag,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<PostDto> posts = postService.findAll(pageable, tag);
        model.addAttribute("posts", posts.getContent());
        model.addAttribute("paging", posts);
        return "posts";
    }

    @GetMapping("/add")
    public String addPage() {
        return "add-post";
    }

    @GetMapping("/{id}")
    public String post(@PathVariable("id") Long id, Model model) {
        PostDto foundPost = postService.findById(id);
        model.addAttribute("post", foundPost);
        return "post";
    }

    @PostMapping
    public String addPost(
            @RequestParam(name = "title") String title,
            @RequestParam(name = "image") MultipartFile image,
            @RequestParam(name = "tags") String tags,
            @RequestParam(name = "text") String text
    ) {
        postService.save(title, text, tags, image);
        return "redirect:/posts";
    }

    @PostMapping("/{id}")
    public String updatePost(
            @PathVariable("id") Long postId,
            @RequestParam(name = "title") String title,
            @RequestParam(name = "image") MultipartFile image,
            @RequestParam(name = "tags") String tags,
            @RequestParam(name = "text") String text
    ) {
        postService.update(postId, title, text, tags, image);
        return "redirect:/posts";
    }

    @GetMapping("/{id}/edit")
    public String updatePage(@PathVariable("id") Long postId, Model model) {
        PostDto postDto = postService.findById(postId);
        model.addAttribute("post", postDto);
        return "add-post";
    }

    @PostMapping("/{id}/delete")
    public String deletePost(@PathVariable("id") Long postId) {
        postService.delete(postId);
        return "redirect:/posts";
    }

    @PostMapping("/{id}/like")
    public String like(@PathVariable("id") Long id, @RequestParam(name = "like") boolean likeValue) {
        postService.like(id, likeValue);
        return "redirect:/posts";
    }

    @PostMapping("/{id}/comments")
    public String addComment(@PathVariable("id") Long id, @RequestParam(name = "text") String text) {
        postService.addComment(id, text);
        return "redirect:/posts";
    }

    @PostMapping("/{postId}/comments/{commentId}")
    public String updateComment(
            @PathVariable("postId") Long postId,
            @RequestParam(name = "text") String text,
            @PathVariable("commentId") Long commentId
    ) {
        postService.updateComment(postId, text, commentId);
        return "redirect:/posts";
    }

    @PostMapping("/{postId}/comments/{commentId}/delete")
    public String deleteComment(
            @PathVariable("postId") Long postId,
            @PathVariable("commentId") Long commentId
    ) {
        postService.deleteComment(postId, commentId);
        return "redirect:/posts";
    }
}