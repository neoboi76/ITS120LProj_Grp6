package com.newscheck.newscheck.services.interfaces;

import com.newscheck.newscheck.models.UserModel;
import com.newscheck.newscheck.models.enums.AuditAction;
import com.newscheck.newscheck.models.requests.AuditLogDTO;
import com.newscheck.newscheck.models.requests.AuditLogFilterDTO;
import com.newscheck.newscheck.models.responses.AuditLogResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface IAuditLogService {

    void log(AuditAction action, UserModel user, String details, HttpServletRequest request);

    void log(AuditAction action, Long userId, String details, HttpServletRequest request);

    void log(AuditAction action, UserModel user, String details, Long verificationId, HttpServletRequest request);

    void verLog(AuditAction action, Long verificationId, String details, HttpServletRequest request);

    void logError(AuditAction action, UserModel user, String errorMessage, HttpServletRequest request);

    List<AuditLogDTO> getUserLogs(Long userId);

    List<AuditLogDTO> getLogsByAction(AuditAction action);

    List<AuditLogDTO> getLogsByDateRange(LocalDateTime start, LocalDateTime end);

    List<AuditLogDTO> getRecentLogs(int limit);

    long getUserActionCount(Long userId);

    Page<AuditLogResponseDTO> getAllAuditLogs(Pageable pageable, AuditLogFilterDTO filter);

    Page<AuditLogResponseDTO> getAuditLogsByUserId(Long userId, Pageable pageable);

    Page<AuditLogResponseDTO> getAuditLogsByVerificationId(Long verificationId, Pageable pageable);

    Map<String, Object> getAuditLogStatistics();

    long deleteAuditLogsOlderThan(LocalDateTime cutoffDate);

    List<Map<String, Object>> getAllUsersWithLogCounts();
}