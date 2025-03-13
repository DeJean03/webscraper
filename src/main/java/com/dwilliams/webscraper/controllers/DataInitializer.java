package com.dwilliams.webscraper.controllers;

import com.dwilliams.webscraper.models.User;
import com.dwilliams.webscraper.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;




@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        initializeAdminUser();
    }

    private void initializeAdminUser() {
        String defaultUsername = System.getenv("DEFAULT_ADMIN_USERNAME");
        String defaultPassword = System.getenv("DEFAULT_ADMIN_PASSWORD");

        // Fallbacks if environment variables are not set
        if (defaultUsername == null) {
            defaultUsername = "admin";
        }
        if (defaultPassword == null) {
            defaultPassword = "password123";
        }

        if (userRepository.findByUsername(defaultUsername).isEmpty()) {
            User admin = new User();
            admin.setUsername(defaultUsername);
            admin.setPassword(passwordEncoder.encode(defaultPassword));
            userRepository.save(admin);
            System.out.println("Default admin user created: " + defaultUsername);
        }
    }
}
