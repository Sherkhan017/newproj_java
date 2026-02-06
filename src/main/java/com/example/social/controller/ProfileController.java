package com.example.social.controller;

import com.example.social.domain.Account;
import com.example.social.domain.Profile;
import com.example.social.dto.ProfileRequest;
import com.example.social.service.ProfileService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {
    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping
    public Profile create(@RequestBody ProfileRequest request) {
        Profile profile = new Profile.Builder()
                .username(request.username())
                .bio(request.bio())
                .interests(request.interests())
                .build();
        return profileService.create(profile);
    }

    @GetMapping
    public List<Profile> getAll() {
        return profileService.getAll();
    }

    @GetMapping("/{id}")
    public Profile getById(@PathVariable Long id) {
        return profileService.getById(id);
    }

    @GetMapping("/search")
    public List<Profile> search(@RequestParam String username) {
        return profileService.searchByUsername(username);
    }

    @GetMapping("/sorted/accounts")
    public List<Account> sortedAccounts() {
        return profileService.sortedAccountsByUsername();
    }

    @PutMapping("/{id}")
    public Profile update(@PathVariable Long id, @RequestBody ProfileRequest request) {
        Profile profile = new Profile.Builder()
                .username(request.username())
                .bio(request.bio())
                .interests(request.interests())
                .build();
        return profileService.update(id, profile);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        profileService.delete(id);
    }
}
