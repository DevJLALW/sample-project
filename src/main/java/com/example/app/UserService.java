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
        logger.info("User updated successfully");
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
