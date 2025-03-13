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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/scraper")
public class ScraperController {

    private final ScraperService scraperService;
    private final ScrapedDataService scrapedDataService;

    @Autowired
    public ScraperController(ScraperService scraperService, ScrapedDataService scrapedDataService) {
        this.scraperService = scraperService;
        this.scrapedDataService = scrapedDataService;
    }

    // GET: Return the main scraping page (view)
    @GetMapping(value = "/start")
    public String getScrapePage(Model model) {
        model.addAttribute("message", "Start your scraping task here");
        return "StartScraping"; // View name
    }

    // POST: Start a scraping task
    @PostMapping(value = "/start")
    public String startScraping(@RequestParam String url, Model model) {
        if (!isValidUrl(url)) {
            model.addAttribute("error", "Invalid URL format.");
            return "redirect: /StartScraping"; // Return back to scraping page with error
        }

        try {
            String taskId = scraperService.startScraping(url);
            model.addAttribute("success", "Scraping task started successfully! Task ID: " + taskId);
            return "redirect:/scraping_results?taskId=" + taskId; // Redirect to the results page
        } catch (Exception e) {
            model.addAttribute("error", "Failed to start scraping task.");
            return "StartScraping";
        }
    }

    // GET: Fetch scraping results for a specific task and return view
    @GetMapping("/scraping_results")
    public String getScrapingResults(@RequestParam String taskId, Model model) {
        try {
            List<ScrapedData> results = scraperService.getResultsByTaskId(taskId);

            if (results.isEmpty()) {
                model.addAttribute("message", "No results found for the given Task ID.");
            } else {
                model.addAttribute("results", results);
            }
            return "scraping_results"; // View name
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred while fetching results.");
            return "scraping_results";
        }
    }

    // GET: Fetch all scraped data and return view
    @GetMapping("/data")
    public String getAllScrapedData(Model model) {
        List<ScrapedData> scrapedData = scrapedDataService.getAllScrapedData();

        if (scrapedData.isEmpty()) {
            model.addAttribute("message", "No scraped data available.");
        } else {
            model.addAttribute("scrapedData", scrapedData);
        }

        return "scraped-data-list"; // View name for displaying all scraped data records
    }

    // POST: Add new scraped data
    @PostMapping("/data")
    public String createScrapedData(@Valid @RequestBody ScrapedData scrapedData, Model model) {
        try {
            ScrapedData savedData = scrapedDataService.createScrapedData(scrapedData.getTitle(), scrapedData.getLink());
            model.addAttribute("success", "Scraped data saved successfully!");
            return "redirect:/scraper/data"; // Redirect to all data view
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred while saving scraped data.");
            return "scraped-data-form"; // Return to a form page (create data form)
        }
    }

    // POST: Helper endpoint to validate and save data
    @PostMapping("/data/add")
    public String saveData(@RequestParam String title, @RequestParam String link, Model model) {
        if (!isValidUrl(link) || title.isBlank()) {
            model.addAttribute("error", "Title and link must be valid and non-empty.");
            return "scraped-data-form"; // Return to the data form with error
        }

        try {
            scrapedDataService.saveScrapedData(title, link);
            model.addAttribute("success", "Data saved successfully!");
            return "redirect:/scraper/data"; // Redirect to the scraped data list view
        } catch (Exception e) {
            model.addAttribute("error", "Failed to save data.");
            return "scraped-data-form"; // Return to the form page
        }
    }

    // Utility: Validate URL format
    private boolean isValidUrl(String url) {
        try {
            new URL(url); // Check if valid
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }
}