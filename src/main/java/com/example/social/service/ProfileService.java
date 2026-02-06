package com.example.social.service;

import com.example.social.domain.Account;
import com.example.social.domain.Profile;

import java.util.List;

public interface ProfileService {
    Profile create(Profile profile);
    List<Profile> getAll();
    Profile getById(Long id);
    Profile update(Long id, Profile profile);
    void delete(Long id);
    List<Profile> searchByUsername(String part);
    List<Account> sortedAccountsByUsername();
}
