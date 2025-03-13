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

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
    }


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    public Optional<User> getUserById(Long userId) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID.");
        }
        return userRepository.findById(userId);
    }


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