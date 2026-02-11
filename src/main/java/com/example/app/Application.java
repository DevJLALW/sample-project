package com.example.app;

import java.util.Date;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Main application class demonstrating legacy patterns and code for OpenRewrite analysis
 */
public class Application {
    private static final Logger logger = LogManager.getLogger(Application.class);
    
    public static void main(String[] args) {
        logger.info("Starting application...");
        
        Application app = new Application();
        app.demonstrateLegacyPatterns();
        
        logger.info("Application completed successfully");
    }
    
    public void demonstrateLegacyPatterns() {
        // Legacy date pattern
        Date date = new Date();
        logger.info("Current date (legacy): {}", date.toString());
        
        // Using StringBuffer instead of StringBuilder
        StringBuffer sb = new StringBuffer();
        sb.append("Building string");
        sb.append(" with legacy API");
        logger.debug("Result: {}", sb.toString());
        
        // Service initialization
        UserService userService = new UserService();
        User user = userService.createUser("John Doe", "john@example.com");
        logger.info("Created user: {}", user.getName());
    }
}
