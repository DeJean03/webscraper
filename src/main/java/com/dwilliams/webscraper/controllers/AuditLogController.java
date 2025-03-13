package com.dwilliams.webscraper.controllers;
import com.dwilliams.webscraper.models.AuditLog;
import com.dwilliams.webscraper.services.AuditLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/audit-logs")
public class AuditLogController {

    private static final Logger log = LoggerFactory.getLogger(AuditLogController.class);

    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @PostMapping
    public ResponseEntity<?> createAuditLog(@RequestParam String action, @RequestParam Long userId) {
        if (action == null || action.isBlank()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Action cannot be null or blank");
        }

        try {
            AuditLog createdLog = auditLogService.createAuditLog(action, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdLog);
        } catch (IllegalArgumentException e) {
            log.error("Invalid parameters passed for audit log creation", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid parameters");
        } catch (Exception e) {
            log.error("Unexpected error during audit log creation", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create audit log");
        }
    }

    @GetMapping
    public ResponseEntity<List<AuditLog>> getAllAuditLogs() {
        List<AuditLog> logs = auditLogService.getAllAuditLogs();
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/by-date")
    public ResponseEntity<?> getLogsByDateRange(@RequestParam String from, @RequestParam String to) {
        try {
            LocalDateTime fromDateTime = LocalDateTime.parse(from);
            LocalDateTime toDateTime = LocalDateTime.parse(to);

            if (fromDateTime.isAfter(toDateTime)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Start date must be before end date");
            }

            List<AuditLog> logs = auditLogService.getLogsByDateRange(fromDateTime, toDateTime);
            return ResponseEntity.ok(logs);

        } catch (DateTimeParseException e) {
            log.error("Invalid date format provided", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid date format. Use ISO-8601 format.");
        } catch (Exception e) {
            log.error("Unexpected error during log retrieval by date range", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve logs");
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AuditLog>> getLogsByUser(@PathVariable Long userId) {
        List<AuditLog> logs = auditLogService.getLogsByUserId(userId);
        return ResponseEntity.ok(logs);
    }

    @DeleteMapping("/delete-older-than")
    public ResponseEntity<?> deleteOldLogs(@RequestParam String date) {
        try {
            LocalDateTime cutoffDate = LocalDateTime.parse(date);
            auditLogService.deleteLogsOlderThan(cutoffDate);
            return ResponseEntity.noContent().build();
        } catch (DateTimeParseException e) {
            log.error("Invalid date format for deletion cutoff date", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid date format. Use ISO-8601 format.");
        } catch (Exception e) {
            log.error("Unexpected error during deletion of old logs", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete old logs");
        }
    }
}


