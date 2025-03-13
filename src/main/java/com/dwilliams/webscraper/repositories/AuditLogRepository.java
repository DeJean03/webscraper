package com.dwilliams.webscraper.repositories;

import com.dwilliams.webscraper.models.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findByTimestampBetween(LocalDateTime start, LocalDateTime end);

    List<AuditLog> findByUserId(Long userId);


    void deleteByTimestampBefore(LocalDateTime date);




}