package com.example.app;

import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Service for user operations demonstrating various code patterns
 */
public class UserService {
    private static final Logger logger = LogManager.getLogger(UserService.class);
    private static final Gson gson = new Gson();

    private final DatabaseClient databaseClient;

    public UserService() {
        this.databaseClient = null;
    }

    public UserService(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    public User createUser(String name, String email) {
        logger.debug("Creating user with name: {} and email: {}", name, email);

        if (StringUtils.isBlank(name) || StringUtils.isBlank(email)) {
            logger.warn("Invalid user input - name or email is blank");
            throw new IllegalArgumentException("Name and email cannot be blank");
        }

        User user = new User();
        user.setId(generateId());
        user.setName(name);
        user.setEmail(email);

        logger.info("User created successfully: {}", user.getName());
        return user;
    }

    /**
     * Creates a user and persists it when a database client is configured.
     */
    public User createAndPersistUser(String name, String email) {
        User user = createUser(name, email);
        persistUser(user);
        return user;
    }

    public void persistUser(User user) {
        if (databaseClient == null) {
            logger.warn("No database client configured; skipping persistence");
            return;
        }
        databaseClient.createUser(user);
    }

    public User findUserByEmail(String email) {
        if (databaseClient == null) {
            logger.warn("No database client configured; cannot read from database");
            return null;
        }
        return databaseClient.findByEmail(email);
    }

    public java.util.List<User> listUsers() {
        if (databaseClient == null) {
            logger.warn("No database client configured; returning empty list");
            return java.util.Collections.emptyList();
        }
        return databaseClient.listUsers();
    }

    public String serializeUser(User user) {
        logger.debug("Serializing user: {}", user.getName());
        return gson.toJson(user);
    }

    public User deserializeUser(String json) {
        logger.debug("Deserializing user from JSON");
        User user = gson.fromJson(json, User.class);
        logger.info("User deserialized: {}", user.getName());
        return user;
    }

    public void updateUser(User user, String newEmail) {
        logger.debug("Updating user email from {} to {}", user.getEmail(), newEmail);

        if (!isValidEmail(newEmail)) {
            logger.error("Invalid email format: {}", newEmail);
            throw new IllegalArgumentException("Invalid email format");
        }

        user.setEmail(newEmail);

        if (databaseClient != null) {
            databaseClient.updateEmail(user.getId(), newEmail);
        } else {
            logger.warn("No database client configured; update only applied in memory");
        }

        logger.info("User updated successfully");
    }

    public void deleteUser(long userId) {
        if (databaseClient == null) {
            logger.warn("No database client configured; delete skipped");
            return;
        }
        databaseClient.deleteUser(userId);
    }

    private boolean isValidEmail(String email) {
        // Simple email validation
        return email.contains("@") && email.contains(".");
    }

    private long generateId() {
        // Simple ID generation
        return System.currentTimeMillis();
    }
}
