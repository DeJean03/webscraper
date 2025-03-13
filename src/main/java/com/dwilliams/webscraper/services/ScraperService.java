package com.dwilliams.webscraper.services;

import com.dwilliams.webscraper.models.ScrapedData;
import com.dwilliams.webscraper.repositories.ScrapedDataRepository;
import com.dwilliams.webscraper.utils.TaskIdGenerator;
import com.dwilliams.webscraper.utils.TaskManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class ScraperService {

    private static final Logger logger = LoggerFactory.getLogger(ScraperService.class);

    @Autowired
    private ScrapedDataService scrapedDataService;

    @Autowired
    private ScrapedDataRepository scrapedDataRepository;

    @Autowired
    private TaskManager taskManager;


    public String startScraping(String url) {
        if (url == null || url.isBlank() || !url.startsWith("http")) {
            throw new IllegalArgumentException("Invalid URL: " + url);
        }

        String taskId = TaskIdGenerator.generateTaskId();

        taskManager.registerTask(taskId);

        new Thread(() -> {
            try {
                logger.info("Starting scraping for URL: {}", url);
                List<ScrapedData> scrapingResults = performScraping(url);

                scrapedDataService.saveAllScrapedData(scrapingResults);
                taskManager.updateTaskResults(taskId, scrapingResults);
                logger.info("Successfully scraped and saved {} links for taskId: {}", scrapingResults.size(), taskId);
            } catch (Exception e) {
                logger.error("Error while scraping URL: {} for taskId: {}", url, taskId, e);
            }
        }).start();

        return taskId;
    }


    private List<ScrapedData> performScraping(String url) {
        List<ScrapedData> scrapedDataList = new ArrayList<>();
        try {
            Document document = Jsoup.connect(url).get();

            for (Element element : document.select("a[href]")) {
                String link = element.attr("abs:href");
                String title = element.text();

                ScrapedData data = new ScrapedData();
                data.setTitle(title);
                data.setLink(link);
                scrapedDataList.add(data);
            }

        } catch (Exception e) {
            logger.error("Error during scraping process for URL: {}", url, e);
        }
        return scrapedDataList;
    }



    public List<ScrapedData> getResultsByTaskId(String taskId) {
        return taskManager.getTaskResults(taskId);
    }



    public List<ScrapedData> searchByKeyword(String keyword) {
        logger.info("Searching for scraped data with keyword: {}", keyword);
        return scrapedDataRepository.findByTitleContainingIgnoreCase(keyword);
    }


    public boolean checkIfTitleExists(String title) {
        logger.info("Checking if title exists: {}", title);
        return scrapedDataRepository.existsByTitle(title);
    }


    public List<ScrapedData> searchByTitleAndLink(String title, String link) {
        logger.info("Searching for scraped data with title '{}' and link '{}'", title, link);
        return scrapedDataRepository.findByTitleAndLink(title, link);
    }


    public void deleteByLink(String link) {
        logger.info("Deleting scraped data with link: {}", link);
        scrapedDataRepository.deleteByLink(link);
    }


    public Optional<ScrapedData> findByLink(String link) {
        logger.info("Finding scraped data by link: {}", link);
        return scrapedDataRepository.findByLink(link);
    }

    public long countByTitle(String title) {
        logger.info("Counting scraped data entries with title: {}", title);
        return scrapedDataRepository.countByTitle(title);
    }
}