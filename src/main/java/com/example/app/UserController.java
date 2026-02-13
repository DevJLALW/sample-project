package com.example.app;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for User CRUD operations
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final DatabaseClient databaseClient;

    public UserController(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest request) {
        UserService userService = new UserService(databaseClient);
        User user = userService.createAndPersistUser(request.getName(), request.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping
    public ResponseEntity<List<User>> listUsers() {
        UserService userService = new UserService(databaseClient);
        List<User> users = userService.listUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        UserService userService = new UserService(databaseClient);
        User user = userService.findUserByEmail(email);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable long id, @RequestBody UpdateUserRequest request) {
        UserService userService = new UserService(databaseClient);
        User user = databaseClient.listUsers().stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .orElse(null);
        
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        
        userService.updateUser(user, request.getEmail());
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) {
        UserService userService = new UserService(databaseClient);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // Request DTOs
    public static class CreateUserRequest {
        private String name;
        private String email;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    public static class UpdateUserRequest {
        private String email;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
