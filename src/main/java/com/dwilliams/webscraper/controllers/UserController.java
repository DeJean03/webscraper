package com.dwilliams.webscraper.controllers;

import com.dwilliams.webscraper.models.User;
import com.dwilliams.webscraper.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        // Check for null fields or empty values
        if (user.getUsername() == null || user.getUsername().isBlank() ||
                user.getPassword() == null || user.getPassword().isBlank() ||
                user.getEmail() == null || user.getEmail().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid data. All fields are required.");
        }

        try {
            // Register the new user
            userService.registerUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully!");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while registering the user.");
        }
    }


    @PostMapping("users/create")
    public ResponseEntity<User> createUser(@RequestParam String username,
                                           @RequestParam String password,
                                           @RequestParam String email) {
        if (username == null || username.isBlank() ||
                password == null || password.isBlank() ||
                email == null || email.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }

        try {
            User user = userService.createUser(username, password, email, null);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(users);
        }
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        if (userId == null || userId <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }

        return userService.getUserById(userId)
                .map(user -> ResponseEntity.status(HttpStatus.OK).body(user))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        if (userId == null || userId <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid user ID.");
        }

        try {
            userService.deleteUser(userId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body("User deleted successfully.");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while deleting the user.");
        }
    }

}