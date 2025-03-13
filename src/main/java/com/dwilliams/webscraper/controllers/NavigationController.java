package com.dwilliams.webscraper.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


    @Controller
    public class NavigationController {

        @GetMapping("/register")
        public String showRegisterPage() {
            return "register";
        }

        @GetMapping("/login")
        public String showLoginPage() {
            return "login";
        }



    }
