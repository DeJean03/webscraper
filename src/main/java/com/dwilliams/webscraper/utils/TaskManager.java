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

    /**
     * Registers a new task with the given taskId.
     *
     * @param taskId the unique task identifier
     */
    public void registerTask(String taskId) {
        taskDataStore.put(taskId, null); // Initialize task with no results yet
    }

    /**
     * Updates task results after scraping.
     *
     * @param taskId the unique task identifier
     * @param results the list of results for the task
     */
    public void updateTaskResults(String taskId, List<ScrapedData> results) {
        taskDataStore.put(taskId, results);
    }

    /**
     * Fetches results for a given task ID.
     *
     * @param taskId the unique task identifier
     * @return the list of results or null if not found
     */
    public List<ScrapedData> getTaskResults(String taskId) {
        return taskDataStore.get(taskId);
    }

    /**
     * Checks if a task exists for the given task ID.
     *
     * @param taskId the unique task identifier
     * @return true if the task exists, false otherwise
     */
    public boolean taskExists(String taskId) {
        return taskDataStore.containsKey(taskId);
    }
}