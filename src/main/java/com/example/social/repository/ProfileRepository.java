package com.example.social.repository;

import com.example.social.domain.Profile;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository {
    Profile save(Profile profile);
    List<Profile> findAll();
    Optional<Profile> findById(Long id);
    Profile update(Long id, Profile profile);
    void delete(Long id);
}
