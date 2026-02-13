package com.example.app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

/**
 * Lightweight JDBC helper wired to an in-memory H2 database for demo CRUD.
 */
@Component
public class DatabaseClient {
    private static final Logger logger = LogManager.getLogger(DatabaseClient.class);

    private final String url;
    private final String username;
    private final String password;

    public DatabaseClient(AppConfig config) {
        this.url = config.getDbUrl();
        this.username = config.getDbUsername();
        this.password = config.getDbPassword();
        loadDriver(config.getDbDriver());
    }

    private void loadDriver(String driverClass) {
        try {
            Class.forName(driverClass);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Database driver not found: " + driverClass, e);
        }
    }

    @PostConstruct
    public void initialize() {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "id BIGINT PRIMARY KEY, " +
                "name VARCHAR(255) NOT NULL, " +
                "email VARCHAR(255) NOT NULL UNIQUE" +
                ")";

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            logger.info("Database initialized and 'users' table is ready");
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to initialize database", e);
        }
    }

    public void createUser(User user) {
        String sql = "INSERT INTO users (id, name, email) VALUES (?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, user.getId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getEmail());
            ps.executeUpdate();
            logger.info("User persisted: {}", user.getEmail());
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to create user", e);
        }
    }

    public List<User> listUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT id, name, email FROM users ORDER BY id";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                users.add(new User(rs.getLong("id"), rs.getString("name"), rs.getString("email")));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to list users", e);
        }
        return users;
    }

    public User findByEmail(String email) {
        String sql = "SELECT id, name, email FROM users WHERE email = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getLong("id"), rs.getString("name"), rs.getString("email"));
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to find user by email", e);
        }
        return null;
    }

    public void updateEmail(long userId, String newEmail) {
        String sql = "UPDATE users SET email = ? WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newEmail);
            ps.setLong(2, userId);
            ps.executeUpdate();
            logger.info("User {} email updated to {}", userId, newEmail);
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to update user email", e);
        }
    }

    public void deleteUser(long userId) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.executeUpdate();
            logger.info("User {} deleted", userId);
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to delete user", e);
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}