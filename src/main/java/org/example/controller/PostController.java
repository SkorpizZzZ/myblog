package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.domain.Post;
import org.example.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Post> posts = postService.findAll(pageable);
        model.addAttribute("posts", posts.getContent());
        model.addAttribute("paging", posts);
        return "posts";
    }

    @GetMapping("/add")
    public String add() {
        return "add-post";
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
}