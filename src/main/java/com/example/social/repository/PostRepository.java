package com.example.social.repository;

import com.example.social.domain.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    Post save(Post post);
    List<Post> findAll();
    Optional<Post> findById(Long id);
    List<Post> findByProfileId(Long profileId);
    Post update(Long id, Post post);
    void delete(Long id);
}
