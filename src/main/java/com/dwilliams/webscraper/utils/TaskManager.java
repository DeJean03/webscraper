package com.dwilliams.webscraper.utils;

import com.dwilliams.webscraper.models.ScrapedData;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TaskManager {

    // Store task IDs and their corresponding scraping results
    private final Map<String, List<ScrapedData>> taskDataStore = new ConcurrentHashMap<>();


    public void registerTask(String taskId) {
        taskDataStore.put(taskId, null); // Initialize task with no results yet
    }


    public void updateTaskResults(String taskId, List<ScrapedData> results) {
        taskDataStore.put(taskId, results);
    }


    public List<ScrapedData> getTaskResults(String taskId) {
        return taskDataStore.get(taskId);
    }


    public boolean taskExists(String taskId) {
        return taskDataStore.containsKey(taskId);
    }
}