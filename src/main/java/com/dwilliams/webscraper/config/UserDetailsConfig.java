package com.dwilliams.webscraper.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

    @Configuration
    public class UserDetailsConfig {

        @Bean
        public UserDetailsService userDetailsService() {
            return new InMemoryUserDetailsManager(
                    User.withUsername("admin")
                            .password(new BCryptPasswordEncoder().encode("password123"))
                            .roles("ADMIN")
                            .build()
            );
        }
    }


