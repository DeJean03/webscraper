package com.dwilliams.webscraper.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class NavController {


    @GetMapping("/")
    public String showHome() {
        return "home";
    }

    @GetMapping("/dashboard")
    public String showDashboard() {
        return "dashboard";
    }

    @GetMapping("/scraping-results")
    public String showScrapingResults() {
        return "scraping_results";
    }
}
