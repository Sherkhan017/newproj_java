package com.example.social.service;

import com.example.social.domain.Post;

import java.util.List;

public interface PostService {
    Post create(Post post);
    List<Post> getAll();
    Post getById(Long id);
    Post update(Long id, Post post);
    void delete(Long id);
    List<Post> findByProfileId(Long profileId);
    List<Post> sortByCreatedAtDesc();
}
