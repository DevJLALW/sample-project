package com.example.app;

import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Main Spring Boot application class demonstrating legacy patterns and code for OpenRewrite analysis
 */
@SpringBootApplication
public class Application {
    private static final Logger logger = LogManager.getLogger(Application.class);
    
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
    @Bean
    public CommandLineRunner demoRunner(ApiClient apiClient, DatabaseClient databaseClient) {
        return args -> {
            logger.info("Starting demo flow...");
            demonstrateLegacyPatterns();
            demoApiCall(apiClient);
            demoDatabaseCrud(databaseClient);
            logger.info("Demo flow completed successfully");
        };
    }
    
    private void demonstrateLegacyPatterns() {
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

    private void demoApiCall(ApiClient apiClient) {
        List<String> cycles = apiClient.fetchSpringBootCycles();
        if (cycles.isEmpty()) {
            logger.warn("No Spring Boot cycles returned from API");
        } else {
            logger.info("Spring Boot cycles (first 5): {}", cycles.stream().limit(5).toList());
        }
    }

    private void demoDatabaseCrud(DatabaseClient databaseClient) {
        UserService userService = new UserService(databaseClient);

        // Create
        User created = userService.createAndPersistUser("Jane Doe", "jane@example.com");

        // Read
        User loaded = userService.findUserByEmail("jane@example.com");
        logger.info("Loaded from DB: {}", loaded);

        // Update
        userService.updateUser(created, "jane.doe@example.com");

        // List
        logger.info("All users: {}", userService.listUsers());

        // Delete
        userService.deleteUser(created.getId());
        logger.info("Users after delete: {}", userService.listUsers());
    }
}
