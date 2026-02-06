package com.example.social.domain;

import java.util.Objects;

public abstract class Account {
    private Long id;
    private String username;
    private String bio;

    protected Account(Long id, String username, String bio) {
        this.id = id;
        this.username = username;
        this.bio = bio;
    }

    public abstract String role();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id) && Objects.equals(username, account.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", bio='" + bio + '\'' +
                ", role='" + role() + '\'' +
                '}';
    }
}
