package com.dwilliams.webscraper.controllers;

import com.dwilliams.webscraper.models.ScrapedData;
import com.dwilliams.webscraper.services.ScrapedDataService;
import com.dwilliams.webscraper.services.ScraperService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/scraper")
@CrossOrigin(
        origins = "http://localhost:63342",
        allowCredentials = "true",
        allowedHeaders = "*",
        methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
)
public class ScraperController {

    private static final Logger logger = LoggerFactory.getLogger(ScraperController.class);

    @Autowired
    private ScraperService scraperService;

    @Autowired
    private ScrapedDataService scrapedDataService;

    // ===========================================
    // 1. Web Scraping Methods
    // ===========================================

    /**
     * Starts web scraping for a given URL.
     *
     * @param url The URL to scrape.
     * @return The task ID for scraping.
     */
    @PostMapping("/start")
    public ResponseEntity<String> startScraping(@RequestParam String url) {
        if (!isValidUrl(url)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid URL format.");
        }

        try {
            String taskId = scraperService.startScraping(url);
            logger.info("Scraping started for URL: {} with Task ID: {}", url, taskId);
            return ResponseEntity.ok(taskId);
        } catch (Exception e) {
            logger.error("[startScraping] Error for URL: {}", url, e);
            throw e; // Delegates to GlobalExceptionHandler
        }
    }

    /**
     * Retrieves scraping results for a given Task ID.
     *
     * @param taskId The Task ID to fetch results for.
     * @return A list of scraping results or an error message if not found.
     */
    @GetMapping("/results")
    public ResponseEntity<?> getScrapingResults(@RequestParam String taskId) {
        try {
            List<ScrapedData> results = scraperService.getResultsByTaskId(taskId);

            if (results.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "No scraping results found for the provided Task ID."));
            }

            return ResponseEntity.ok(results);
        } catch (Exception e) {
            logger.error("Error fetching results for Task ID: {}", taskId, e);
            throw e; // Delegates to GlobalExceptionHandler
        }
    }

    /**
     * Redirects to the /scraper/start endpoint while preserving the HTTP method.
     *
     * @param url Optional URL to be passed during redirection.
     * @return A 307 Temporary Redirect response.
     */
    @GetMapping("/start-scraping")
    public ResponseEntity<Void> redirectToStartScraping(@RequestParam(required = false) String url) {
        String location = "/scraper/start";

        // Append the URL parameter if provided
        if (url != null && !url.isBlank()) {
            location += "?url=" + url;
        }

        return ResponseEntity.status(HttpStatus.TEMPORARY_REDIRECT)
                .header("Location", location)
                .build();
    }

    // ===========================================
    // 2. CRUD Operations for Scraped Data
    // ===========================================

    /**
     * Retrieves all scraped data.
     *
     * @return A list of all scraped data or no content if empty.
     */
    @GetMapping("/data")
    public ResponseEntity<List<ScrapedData>> getAllScrapedData() {
        List<ScrapedData> scrapedData = scrapedDataService.getAllScrapedData();

        if (scrapedData.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(scrapedData);
    }

    /**
     * Creates a new scraped data entry.
     *
     * @param scrapedData The scraped data to create.
     * @return The created scraped data or an error response.
     */
    @PostMapping("/data")
    public ResponseEntity<ScrapedData> createScrapedData(@Valid @RequestBody ScrapedData scrapedData) {
        try {
            ScrapedData savedData = scrapedDataService.createScrapedData(scrapedData.getTitle(), scrapedData.getLink());
            logger.info("Scraped data created successfully: {}", savedData);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedData);
        } catch (Exception e) {
            logger.error("Error creating scraped data", e);
            throw e; // Delegates to GlobalExceptionHandler
        }
    }

    /**
     * Adds scraped data by specifying the title and link.
     *
     * @param title The title of the scraped data.
     * @param link  The link of the scraped data.
     * @return A success response or an error response for invalid input.
     */
    @PostMapping("/data/add")
    public ResponseEntity<String> saveData(
            @RequestParam @NotBlank String title,
            @RequestParam @NotBlank String link
    ) {
        if (!isValidUrl(link)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid URL format for the link.");
        }

        if (title.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid title. Title cannot be null or empty.");
        }

        try {
            scrapedDataService.saveScrapedData(title, link);
            logger.info("Scraped data added successfully: Title={}, Link={}", title, link);
            return ResponseEntity.status(HttpStatus.CREATED).body("Data saved successfully!");
        } catch (IllegalArgumentException e) {
            logger.error("Error adding scraped data: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error occurred while saving data", e);
            throw e; // Delegates to GlobalExceptionHandler
        }
    }

    // ===========================================
    // 3. Utility Methods
    // ===========================================

    /**
     * Validates if a given URL is in the correct format.
     *
     * @param url The URL to validate.
     * @return True if valid, false otherwise.
     */
    private boolean isValidUrl(String url) {
        if (url == null || url.isBlank()) {
            return false;
        }

        try {
            new URL(url);
            return true;
        } catch (MalformedURLException e) {
            logger.warn("Invalid URL format: {}", url, e);
            return false;
        }
    }
}