package com.example.social.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Post {
    private Long id;
    private Long profileId;
    private String content;
    private LocalDateTime createdAt;

    public Post(Long id, Long profileId, String content, LocalDateTime createdAt) {
        this.id = id;
        this.profileId = profileId;
        this.content = content;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Post post)) return false;
        return Objects.equals(id, post.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", profileId=" + profileId +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
