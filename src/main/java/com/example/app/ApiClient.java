package com.example.app;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * Tiny HTTP client to fetch data from the public Spring Boot end-of-life API.
 */
@Component
public class ApiClient {
    private static final Logger logger = LogManager.getLogger(ApiClient.class);
    private static final Gson gson = new Gson();

    private final HttpClient httpClient;
    private final String apiUrl;

    public ApiClient(AppConfig config) {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
        this.apiUrl = config.getSpringBootApiUrl();
    }

    /**
     * Fetches Spring Boot release cycles from endoflife.date.
     */
    public List<String> fetchSpringBootCycles() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .timeout(Duration.ofSeconds(10))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                Type listType = new TypeToken<List<Map<String, Object>>>() { }.getType();
                List<Map<String, Object>> payload = gson.fromJson(response.body(), listType);
                return payload.stream()
                        .map(entry -> (String) entry.getOrDefault("cycle", "unknown"))
                        .toList();
            }

            logger.warn("Unexpected response from API: status={} body={}", response.statusCode(), response.body());
        } catch (Exception ex) {
            logger.error("Failed to call Spring Boot EOL API", ex);
        }
        return Collections.emptyList();
    }
}