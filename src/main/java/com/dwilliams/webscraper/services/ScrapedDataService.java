package com.dwilliams.webscraper.services;

import com.dwilliams.webscraper.models.ScrapedData;
import com.dwilliams.webscraper.repositories.ScrapedDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class ScrapedDataService {

    private static final Logger logger = LoggerFactory.getLogger(ScrapedDataService.class);

    @Autowired
    private ScrapedDataRepository scrapedDataRepository;

    // Method to get all scraped data
    public List<ScrapedData> getAllScrapedData() {
        return scrapedDataRepository.findAll();
    }

    public ScrapedData createScrapedData(String title, String link) {
        if (title == null || title.trim().isEmpty() || link == null || link.trim().isEmpty()) {
            throw new IllegalArgumentException("Title and Link cannot be null or empty.");
        }

        ScrapedData scrapedData = new ScrapedData();
        scrapedData.setTitle(title);
        scrapedData.setLink(link);

        return scrapedDataRepository.save(scrapedData);
    }

    public void saveScrapedData(String title, String link) {
        if (title == null || title.trim().isEmpty() || link == null || link.trim().isEmpty()) {
            throw new IllegalArgumentException("Title and Link cannot be null or empty.");
        }

        ScrapedData scrapedData = new ScrapedData();
        scrapedData.setTitle(title);
        scrapedData.setLink(link);
        scrapedDataRepository.save(scrapedData);

    }
    public void saveAllScrapedData(List<ScrapedData> scrapedDataList) {
        if (scrapedDataList != null && !scrapedDataList.isEmpty()) {
            scrapedDataRepository.saveAll(scrapedDataList);
        }
    }




    }
