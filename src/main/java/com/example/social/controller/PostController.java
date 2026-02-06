package com.example.social.controller;

import com.example.social.domain.Post;
import com.example.social.dto.PostRequest;
import com.example.social.service.PostService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public Post create(@RequestBody PostRequest request) {
        return postService.create(new Post(null, request.profileId(), request.content(), LocalDateTime.now()));
    }

    @GetMapping
    public List<Post> getAll() {
        return postService.getAll();
    }

    @GetMapping("/{id}")
    public Post getById(@PathVariable Long id) {
        return postService.getById(id);
    }

    @GetMapping("/profile/{profileId}")
    public List<Post> findByProfileId(@PathVariable Long profileId) {
        return postService.findByProfileId(profileId);
    }

    @GetMapping("/sorted/newest")
    public List<Post> sortNewest() {
        return postService.sortByCreatedAtDesc();
    }

    @PutMapping("/{id}")
    public Post update(@PathVariable Long id, @RequestBody PostRequest request) {
        return postService.update(id, new Post(id, request.profileId(), request.content(), LocalDateTime.now()));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        postService.delete(id);
    }
}
