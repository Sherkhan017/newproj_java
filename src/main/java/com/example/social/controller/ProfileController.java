package com.example.social.controller;

import com.example.social.domain.Account;
import com.example.social.domain.Profile;
import com.example.social.dto.ProfileRequest;
import com.example.social.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {
    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Profile create(@Valid @RequestBody ProfileRequest request) {
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

    @GetMapping("/{id}/metadata")
    public Map<String, Object> metadata(@PathVariable Long id) {
        return profileService.profileMetadata(id);
    }

    @PutMapping("/{id}")
    public Profile update(@PathVariable Long id, @Valid @RequestBody ProfileRequest request) {
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
