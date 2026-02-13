package com.example.app;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Simple configuration loader backed by application.properties.
 */
@Configuration
@PropertySource("classpath:application.properties")
public class AppConfig {
    private static final Logger logger = LogManager.getLogger(AppConfig.class);
    private final Properties properties = new Properties();

    public AppConfig() {
        load();
    }

    private void load() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (is == null) {
                throw new IllegalStateException("application.properties not found on classpath");
            }
            properties.load(is);
        } catch (IOException e) {
            logger.error("Failed to load application.properties", e);
            throw new IllegalStateException("Unable to load configuration", e);
        }
    }

    public String getSpringBootApiUrl() {
        return properties.getProperty("app.api.springBootVersionsUrl");
    }

    public String getDbUrl() {
        return properties.getProperty("app.db.url");
    }

    public String getDbUsername() {
        return properties.getProperty("app.db.username");
    }

    public String getDbPassword() {
        return properties.getProperty("app.db.password");
    }

    public String getDbDriver() {
        return properties.getProperty("app.db.driver");
    }
}