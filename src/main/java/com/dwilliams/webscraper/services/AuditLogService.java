package com.dwilliams.webscraper.services;

import com.dwilliams.webscraper.excptions.UserNotFoundException;
import com.dwilliams.webscraper.models.AuditLog;
import com.dwilliams.webscraper.models.User;
import com.dwilliams.webscraper.repositories.AuditLogRepository;
import com.dwilliams.webscraper.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditLogService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    private UserRepository userRepository;

    public AuditLog createAuditLog(String action, Long userId) {
        AuditLog auditLog = new AuditLog();
        auditLog.setAction(action);
        auditLog.setTimestamp(LocalDateTime.now());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
        auditLog.setUser(user);

        return auditLogRepository.save(auditLog);
    }

    public List<AuditLog> getAllAuditLogs() {
        return auditLogRepository.findAll();
    }

    public List<AuditLog> getLogsByDateRange(LocalDateTime from, LocalDateTime to) {
        return auditLogRepository.findByTimestampBetween(from, to);
    }

    public List<AuditLog> getLogsByUserId(Long userId) {
        return auditLogRepository.findByUserId(userId);
    }

    public void deleteLogsOlderThan(LocalDateTime date) {
        auditLogRepository.deleteByTimestampBefore(date);
    }
}