package com.dwilliams.webscraper.utils;


public class AppConstants {

    // ===========================================
    // 1. Base API URL and Endpoints
    // ===========================================
    public static final String BASE_URL = "https://example.com";
    public static final String SCRAPE_ENDPOINT = "/scrape";
    public static final String DATA_ENDPOINT = "/data";

    // ===========================================
    // 2. Error Messages
    // ===========================================
    public static final String ERROR_SCRAPE_FAILED = "Failed to scrape the website.";
    public static final String ERROR_INVALID_URL = "The provided URL is invalid.";

    // ===========================================
    // 3. Success Messages
    // ===========================================
    public static final String SUCCESS_SCRAPE = "Data scraped successfully!";

    // ===========================================
    // 4. Miscellaneous Constants
    // ===========================================
    public static final String REDIRECT_HOME = "/home";
    public static final String AUDIT_LOG_ACTION = "Scraping Action Performed";

    private AppConstants() {
        // Private constructor to prevent instantiation
    }
}