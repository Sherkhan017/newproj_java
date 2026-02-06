package com.example.social.repository.jdbc;

import com.example.social.domain.Profile;
import com.example.social.repository.ProfileRepository;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcProfileRepository implements ProfileRepository {
    private final DataSource dataSource;

    public JdbcProfileRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Profile save(Profile profile) {
        String sql = "INSERT INTO profiles(username, bio, interests) VALUES(?, ?, ?)";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, profile.getUsername());
            ps.setString(2, profile.getBio());
            ps.setString(3, profile.getInterests());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) profile.setId(rs.getLong(1));
            }
            return profile;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save profile", e);
        }
    }

    @Override
    public List<Profile> findAll() {
        String sql = "SELECT id, username, bio, interests FROM profiles";
        List<Profile> result = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(map(rs));
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read profiles", e);
        }
    }

    @Override
    public Optional<Profile> findById(Long id) {
        String sql = "SELECT id, username, bio, interests FROM profiles WHERE id = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find profile", e);
        }
    }

    @Override
    public Profile update(Long id, Profile profile) {
        String sql = "UPDATE profiles SET username = ?, bio = ?, interests = ? WHERE id = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, profile.getUsername());
            ps.setString(2, profile.getBio());
            ps.setString(3, profile.getInterests());
            ps.setLong(4, id);
            ps.executeUpdate();
            profile.setId(id);
            return profile;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update profile", e);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM profiles WHERE id = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete profile", e);
        }
    }

    private Profile map(ResultSet rs) throws SQLException {
        return new Profile.Builder()
                .id(rs.getLong("id"))
                .username(rs.getString("username"))
                .bio(rs.getString("bio"))
                .interests(rs.getString("interests"))
                .build();
    }
}
