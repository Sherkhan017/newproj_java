package com.example.social.repository.jdbc;

import com.example.social.domain.Post;
import com.example.social.repository.PostRepository;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcPostRepository implements PostRepository {
    private final DataSource dataSource;

    public JdbcPostRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Post save(Post post) {
        String sql = "INSERT INTO posts(profile_id, content, created_at) VALUES(?, ?, ?)";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, post.getProfileId());
            ps.setString(2, post.getContent());
            ps.setTimestamp(3, Timestamp.valueOf(post.getCreatedAt()));
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) post.setId(rs.getLong(1));
            }
            return post;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save post", e);
        }
    }

    @Override
    public List<Post> findAll() {
        String sql = "SELECT id, profile_id, content, created_at FROM posts";
        List<Post> result = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(map(rs));
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read posts", e);
        }
    }

    @Override
    public Optional<Post> findById(Long id) {
        String sql = "SELECT id, profile_id, content, created_at FROM posts WHERE id = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find post", e);
        }
    }

    @Override
    public List<Post> findByProfileId(Long profileId) {
        String sql = "SELECT id, profile_id, content, created_at FROM posts WHERE profile_id = ?";
        List<Post> result = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, profileId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(map(rs));
                }
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find posts by profile", e);
        }
    }

    @Override
    public Post update(Long id, Post post) {
        String sql = "UPDATE posts SET content = ? WHERE id = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, post.getContent());
            ps.setLong(2, id);
            ps.executeUpdate();
            post.setId(id);
            return post;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update post", e);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM posts WHERE id = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete post", e);
        }
    }

    private Post map(ResultSet rs) throws SQLException {
        LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
        return new Post(rs.getLong("id"), rs.getLong("profile_id"), rs.getString("content"), createdAt);
    }
}
