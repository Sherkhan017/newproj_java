package com.example.social.service;

import com.example.social.domain.Account;
import com.example.social.domain.Profile;
import com.example.social.exception.InvalidInputException;
import com.example.social.exception.NotFoundException;
import com.example.social.repository.ProfileRepository;
import com.example.social.util.InMemoryDataPool;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import com.example.social.util.ReflectionInspector;

import java.util.List;
import java.util.Map;

@Service
public class ProfileServiceImpl implements ProfileService {
    private final ProfileRepository profileRepository;
    private final InMemoryDataPool<Profile> dataPool = new InMemoryDataPool<>();

    public ProfileServiceImpl(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public Profile create(Profile profile) {
        validateProfile(profile);
        Profile saved = profileRepository.save(profile);
        refreshPool();
        return saved;
    }

    @Override
    public List<Profile> getAll() {
        refreshPool();
        return dataPool.all();
    }

    @Override
    public Profile getById(Long id) {
        return profileRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Profile not found: " + id));
    }

    @Override
    public Profile update(Long id, Profile profile) {
        validateProfile(profile);
        getById(id);
        Profile updated = profileRepository.update(id, profile);
        refreshPool();
        return updated;
    }

    @Override
    public void delete(Long id) {
        getById(id);
        profileRepository.delete(id);
        refreshPool();
    }

    @Override
    public List<Profile> searchByUsername(String part) {
        refreshPool();
        String normalized = part == null ? "" : part.toLowerCase();
        return dataPool.filter(p -> p.getUsername().toLowerCase().contains(normalized));
    }

    @Override
    public List<Account> sortedAccountsByUsername() {
        refreshPool();
        return dataPool.sort(Comparator.comparing(Profile::getUsername))
                .stream()
                .map(p -> (Account) p)
                .toList();
    }


    @Override
    public Map<String, Object> profileMetadata(Long id) {
        Profile profile = getById(id);
        return Map.of(
                "id", profile.getId(),
                "type", profile.getClass().getSimpleName(),
                "fields", ReflectionInspector.fieldNames(profile.getClass()),
                "role", profile.role());
    }

    private void validateProfile(Profile profile) {
        if (profile.getUsername() == null || profile.getUsername().isBlank()) {
            throw new InvalidInputException("Username must not be blank");
        }
    }

    private void refreshPool() {
        dataPool.replaceAll(profileRepository.findAll());
    }
}
