package com.example.social.service;

import com.example.social.domain.Post;
import com.example.social.domain.PostFactory;
import com.example.social.exception.InvalidInputException;
import com.example.social.exception.NotFoundException;
import com.example.social.repository.PostRepository;
import com.example.social.util.InMemoryDataPool;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final ProfileService profileService;
    private final InMemoryDataPool<Post> dataPool = new InMemoryDataPool<>();

    public PostServiceImpl(PostRepository postRepository, ProfileService profileService) {
        this.postRepository = postRepository;
        this.profileService = profileService;
    }

    @Override
    public Post create(Post post) {
        validatePost(post);
        profileService.getById(post.getProfileId());
        PostFactory factory = new PostFactory(post.getProfileId(), post.getContent());
        Post generated = factory.createAndValidate(this::validatePost);
        generated.setCreatedAt(LocalDateTime.now());
        Post saved = postRepository.save(generated);
        refreshPool();
        return saved;
    }

    @Override
    public List<Post> getAll() {
        refreshPool();
        return dataPool.all();
    }

    @Override
    public Post getById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Post not found: " + id));
    }

    @Override
    public Post update(Long id, Post post) {
        validatePost(post);
        Post existing = getById(id);
        if (!existing.getProfileId().equals(post.getProfileId())) {
            throw new InvalidInputException("Post owner cannot be changed");
        }
        post.setCreatedAt(existing.getCreatedAt());
        Post updated = postRepository.update(id, post);
        refreshPool();
        return updated;
    }

    @Override
    public void delete(Long id) {
        getById(id);
        postRepository.delete(id);
        refreshPool();
    }

    @Override
    public List<Post> findByProfileId(Long profileId) {
        profileService.getById(profileId);
        return postRepository.findByProfileId(profileId);
    }

    @Override
    public List<Post> sortByCreatedAtDesc() {
        refreshPool();
        return dataPool.sort(Comparator.comparing(Post::getCreatedAt).reversed());
    }

    private void validatePost(Post post) {
        if (post.getProfileId() == null) {
            throw new InvalidInputException("Profile id is required");
        }
        if (post.getContent() == null || post.getContent().isBlank()) {
            throw new InvalidInputException("Post content must not be blank");
        }
    }

    private void refreshPool() {
        dataPool.replaceAll(postRepository.findAll());
    }
}
