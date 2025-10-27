package com.newscheck.newscheck.services;

import com.newscheck.newscheck.models.AuditLogModel;
import com.newscheck.newscheck.models.UserModel;
import com.newscheck.newscheck.models.requests.AuditLogDTO;
import com.newscheck.newscheck.models.enums.AuditAction;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface IAuditLogService {

    void log(AuditAction action, UserModel user, String details, HttpServletRequest request);

    void log(AuditAction action, Long userId, String details, HttpServletRequest request);

    void log(AuditAction action, UserModel user, String details, Long verificationId, HttpServletRequest request);

    void logError(AuditAction action, UserModel user, String errorMessage, HttpServletRequest request);

    List<AuditLogDTO> getUserLogs(Long userId);

    List<AuditLogDTO> getLogsByAction(AuditAction action);

    List<AuditLogDTO> getLogsByDateRange(LocalDateTime start, LocalDateTime end);

    List<AuditLogDTO> getRecentLogs(int limit);

    long getUserActionCount(Long userId);

}