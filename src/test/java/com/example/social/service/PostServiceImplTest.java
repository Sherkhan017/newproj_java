package com.example.social.service;

import com.example.social.domain.Post;
import com.example.social.domain.Profile;
import com.example.social.exception.InvalidInputException;
import com.example.social.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PostServiceImplTest {
    private PostServiceImpl postService;

    @BeforeEach
    void setUp() {
        InMemoryProfileService profileService = new InMemoryProfileService();
        profileService.create(new Profile.Builder().username("owner").bio("b").interests("i").build());
        postService = new PostServiceImpl(new InMemoryPostRepository(), profileService);
    }

    @Test
    void createAndSortNewestWorks() {
        postService.create(new Post(null, 1L, "first", null));
        postService.create(new Post(null, 1L, "second", null));

        List<Post> sorted = postService.sortByCreatedAtDesc();
        assertEquals(2, sorted.size());
        assertEquals("second", sorted.get(0).getContent());
    }

    @Test
    void changingOwnerOnUpdateThrows() {
        Post post = postService.create(new Post(null, 1L, "hello", null));
        assertThrows(InvalidInputException.class,
                () -> postService.update(post.getId(), new Post(post.getId(), 2L, "new", LocalDateTime.now())));
    }

    private static class InMemoryPostRepository implements PostRepository {
        private final Map<Long, Post> store = new LinkedHashMap<>();
        private long sequence = 0;

        @Override
        public Post save(Post post) {
            post.setId(++sequence);
            store.put(post.getId(), post);
            return post;
        }

        @Override
        public List<Post> findAll() {
            return new ArrayList<>(store.values());
        }

        @Override
        public Optional<Post> findById(Long id) {
            return Optional.ofNullable(store.get(id));
        }

        @Override
        public List<Post> findByProfileId(Long profileId) {
            return store.values().stream().filter(p -> p.getProfileId().equals(profileId)).toList();
        }

        @Override
        public Post update(Long id, Post post) {
            store.put(id, post);
            return post;
        }

        @Override
        public void delete(Long id) {
            store.remove(id);
        }
    }

    private static class InMemoryProfileService implements ProfileService {
        private final Map<Long, Profile> profiles = new HashMap<>();
        private long seq = 0;

        @Override
        public Profile create(Profile profile) {
            profile.setId(++seq);
            profiles.put(profile.getId(), profile);
            return profile;
        }

        @Override
        public List<Profile> getAll() { return new ArrayList<>(profiles.values()); }

        @Override
        public Profile getById(Long id) {
            Profile profile = profiles.get(id);
            if (profile == null) {
                throw new com.example.social.exception.NotFoundException("Profile not found: " + id);
            }
            return profile;
        }

        @Override
        public Profile update(Long id, Profile profile) { profile.setId(id); profiles.put(id, profile); return profile; }

        @Override
        public void delete(Long id) { profiles.remove(id); }

        @Override
        public List<Profile> searchByUsername(String part) { return List.of(); }

        @Override
        public List<com.example.social.domain.Account> sortedAccountsByUsername() { return List.of(); }

        @Override
        public Map<String, Object> profileMetadata(Long id) { return Map.of(); }
    }
}
