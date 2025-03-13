package com.dwilliams.webscraper.utils;

import java.util.UUID;

public class TaskIdGenerator {


    public static String generateTaskId() {
        return UUID.randomUUID().toString();
    }
}