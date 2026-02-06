package com.example.social.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SocialNetwork {
    private String name;
    private final List<Profile> profiles = new ArrayList<>();
    private final List<Post> posts = new ArrayList<>();

    public SocialNetwork(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Profile> getProfiles() {
        return new ArrayList<>(profiles);
    }

    public List<Post> getPosts() {
        return new ArrayList<>(posts);
    }

    public void addProfile(Profile profile) {
        profiles.add(profile);
    }

    public void addPost(Post post) {
        posts.add(post);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SocialNetwork that)) return false;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "SocialNetwork{" +
                "name='" + name + '\'' +
                ", profiles=" + profiles.size() +
                ", posts=" + posts.size() +
                '}';
    }
}
