package com.dwilliams.webscraper.services;


import com.dwilliams.webscraper.models.AuditLog;
import com.dwilliams.webscraper.models.User;
import com.dwilliams.webscraper.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    @Qualifier("passwordEncoder")
    private PasswordEncoder passwordEncoder;


    @Transactional
    public User createUser(String username, String password, String email, List<AuditLog> logs) {
        // Validate inputs
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank.");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or blank.");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank.");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password)); // Hash the password
        user.setEmail(email);

        // Handle audit logs
        if (logs != null && !logs.isEmpty()) {
            for (AuditLog log : logs) {
                log.setUser(user);
            }
            user.setAuditLogs(logs);
        } else {
            user.setAuditLogs(new ArrayList<>());
        }

        return userRepository.save(user);
    }


    @Transactional
    public void registerUser(User user) {
        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank.");
        }
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or blank.");
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank.");
        }

        // Check if the username is unique
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists.");
        }

        // Check if the email is unique
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists.");
        }

        // Encode the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));


        // Save the user to the database
        userRepository.save(user);
    }

    /**
     * Retrieve all users.
     *
     * @return A list of all users.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Retrieve a user by ID.
     *
     * @param userId The ID of the user.
     * @return An Optional containing the user object if found, or empty if not found.
     */
    public Optional<User> getUserById(Long userId) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID.");
        }
        return userRepository.findById(userId);
    }

    /**
     * Delete a user by ID.
     *
     * @param userId The ID of the user to delete.
     * @throws IllegalArgumentException if the user ID is invalid.
     */
    @Transactional
    public void deleteUser(Long userId) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID.");
        }
        if (!userRepository.existsById(userId)) {
            throw new IllegalStateException("User with ID " + userId + " does not exist.");
        }
        userRepository.deleteById(userId);
    }


    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }
}