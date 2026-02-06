package com.example.social.domain;

import java.time.LocalDateTime;

public class PostFactory implements EntityFactory<Post> {
    private final Long profileId;
    private final String content;

    public PostFactory(Long profileId, String content) {
        this.profileId = profileId;
        this.content = EntityFactory.normalize(content);
    }

    @Override
    public Post create() {
        return new Post(null, profileId, content, LocalDateTime.now());
    }
}
