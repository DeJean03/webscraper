package com.dwilliams.webscraper.utils;

import java.util.UUID;

public class TaskIdGenerator {

    /**
     * Generate a random, unique task ID.
     *
     * @return a unique string representing the task ID
     */
    public static String generateTaskId() {
        return UUID.randomUUID().toString();
    }
}