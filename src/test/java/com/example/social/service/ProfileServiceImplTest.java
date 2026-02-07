package com.example.social.service;

import com.example.social.domain.Profile;
import com.example.social.exception.InvalidInputException;
import com.example.social.exception.NotFoundException;
import com.example.social.repository.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ProfileServiceImplTest {

    private ProfileServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ProfileServiceImpl(new InMemoryProfileRepository());
    }

    @Test
    void createAndSearchWorks() {
        service.create(new Profile.Builder().username("alice").bio("b").interests("java").build());
        service.create(new Profile.Builder().username("bob").bio("b").interests("sql").build());

        List<Profile> found = service.searchByUsername("ali");
        assertEquals(1, found.size());
        assertEquals("alice", found.get(0).getUsername());
    }

    @Test
    void blankUsernameThrows() {
        assertThrows(InvalidInputException.class,
                () -> service.create(new Profile.Builder().username(" ").bio("x").interests("y").build()));
    }

    @Test
    void metadataContainsReflectionFields() {
        Profile profile = service.create(new Profile.Builder().username("meta").bio("x").interests("y").build());
        Map<String, Object> metadata = service.profileMetadata(profile.getId());
        @SuppressWarnings("unchecked")
        List<String> fields = (List<String>) metadata.get("fields");
        assertTrue(fields.contains("interests"));
        assertEquals("Profile", metadata.get("type"));
    }

    @Test
    void missingProfileThrows() {
        assertThrows(NotFoundException.class, () -> service.getById(999L));
    }

    private static class InMemoryProfileRepository implements ProfileRepository {
        private final Map<Long, Profile> store = new LinkedHashMap<>();
        private long sequence = 0;

        @Override
        public Profile save(Profile profile) {
            profile.setId(++sequence);
            store.put(profile.getId(), profile);
            return profile;
        }

        @Override
        public List<Profile> findAll() {
            return new ArrayList<>(store.values());
        }

        @Override
        public Optional<Profile> findById(Long id) {
            return Optional.ofNullable(store.get(id));
        }

        @Override
        public Profile update(Long id, Profile profile) {
            profile.setId(id);
            store.put(id, profile);
            return profile;
        }

        @Override
        public void delete(Long id) {
            store.remove(id);
        }
    }
}
