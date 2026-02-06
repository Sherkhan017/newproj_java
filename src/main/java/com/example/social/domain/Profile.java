package com.example.social.domain;

public class Profile extends Account {
    private String interests;

    private Profile(Builder builder) {
        super(builder.id, builder.username, builder.bio);
        this.interests = builder.interests;
    }

    @Override
    public String role() {
        return "USER";
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public static class Builder {
        private Long id;
        private String username;
        private String bio;
        private String interests;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder username(String username) {
            this.username = EntityFactory.normalize(username);
            return this;
        }

        public Builder bio(String bio) {
            this.bio = EntityFactory.normalize(bio);
            return this;
        }

        public Builder interests(String interests) {
            this.interests = EntityFactory.normalize(interests);
            return this;
        }

        public Profile build() {
            return new Profile(this);
        }
    }
}
