package com.example.app;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Data utility class with various collection operations
 */
public class DataUtil {
    private static final Logger logger = LogManager.getLogger(DataUtil.class);
    
    /**
     * Convert collection to list using raw types (legacy pattern)
     */
    @SuppressWarnings("unchecked")
    public static List convertToList(Collection collection) {
        logger.debug("Converting collection to list");
        return new ArrayList(collection);
    }
    
    /**
     * Create a map of users by email (generic pattern)
     */
    public static Map<String, User> createUserMap(List<User> users) {
        logger.debug("Creating user map from {} users", users.size());
        Map<String, User> userMap = new HashMap<>();
        
        for (User user : users) {
            if (user != null && user.getEmail() != null) {
                userMap.put(user.getEmail(), user);
            }
        }
        
        logger.info("User map created with {} entries", userMap.size());
        return userMap;
    }
    
    /**
     * Check if value exists in collection
     */
    public static <T> boolean contains(Collection<T> collection, T value) {
        if (collection == null || value == null) {
            return false;
        }
        return collection.contains(value);
    }
    
    /**
     * Legacy synchronized pattern
     */
    public synchronized void legacySynchronizedMethod() {
        logger.debug("Executing synchronized method");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            logger.error("Interrupted during sleep", e);
            Thread.currentThread().interrupt();
        }
    }
}
