package com.example.app;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller to expose Spring Boot version information from the external API
 */
@RestController
@RequestMapping("/api/springboot")
public class SpringBootVersionController {

    private final ApiClient apiClient;

    public SpringBootVersionController(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    @GetMapping("/versions")
    public ResponseEntity<SpringBootVersionsResponse> getVersions() {
        List<String> cycles = apiClient.fetchSpringBootCycles();
        SpringBootVersionsResponse response = new SpringBootVersionsResponse(cycles);
        return ResponseEntity.ok(response);
    }

    public static class SpringBootVersionsResponse {
        private List<String> versions;
        private int count;

        public SpringBootVersionsResponse(List<String> versions) {
            this.versions = versions;
            this.count = versions.size();
        }

        public List<String> getVersions() {
            return versions;
        }

        public void setVersions(List<String> versions) {
            this.versions = versions;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}
