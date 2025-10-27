package com.newscheck.newscheck.services;

import com.newscheck.newscheck.models.AuditLogModel;
import com.newscheck.newscheck.models.UserModel;
import com.newscheck.newscheck.models.requests.AuditLogDTO;
import com.newscheck.newscheck.models.enums.AuditAction;
import com.newscheck.newscheck.repositories.AuditLogRepository;
import com.newscheck.newscheck.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuditLogService implements IAuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;

    @Override
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void log(AuditAction action, UserModel user, String details, HttpServletRequest request) {
        AuditLogModel auditLog = new AuditLogModel();
        auditLog.setAction(action);
        auditLog.setUser(user);
        auditLog.setDetails(details);
        auditLog.setIpAddress(getClientIp(request));
        auditLog.setUserAgent(getUserAgent(request));
        auditLog.setTimestamp(LocalDateTime.now());
        auditLog.setSuccess(true);

        auditLogRepository.save(auditLog);
    }

    @Override
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void log(AuditAction action, Long userId, String details, HttpServletRequest request) {
        UserModel user = null;
        if (userId != null) {
            user = userRepository.findById(userId).orElse(null);
        }
        log(action, user, details, request);
    }


    @Override
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void log(AuditAction action, UserModel user, String details, Long verificationId, HttpServletRequest request) {
        AuditLogModel auditLog = new AuditLogModel();
        auditLog.setAction(action);
        auditLog.setUser(user);
        auditLog.setDetails(details);
        auditLog.setVerificationId(verificationId);
        auditLog.setIpAddress(getClientIp(request));
        auditLog.setUserAgent(getUserAgent(request));
        auditLog.setTimestamp(LocalDateTime.now());
        auditLog.setSuccess(true);

        auditLogRepository.save(auditLog);
    }

    @Override
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logError(AuditAction action, UserModel user, String errorMessage, HttpServletRequest request) {
        AuditLogModel auditLog = new AuditLogModel();
        auditLog.setAction(action);
        auditLog.setUser(user);
        auditLog.setErrorMessage(errorMessage);
        auditLog.setIpAddress(getClientIp(request));
        auditLog.setUserAgent(getUserAgent(request));
        auditLog.setTimestamp(LocalDateTime.now());
        auditLog.setSuccess(false);

        auditLogRepository.save(auditLog);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLogDTO> getUserLogs(Long userId) {
        List<AuditLogModel> logs = auditLogRepository.findByUser_UserIdOrderByTimestampDesc(userId);
        return logs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLogDTO> getLogsByAction(AuditAction action) {
        List<AuditLogModel> logs = auditLogRepository.findByActionOrderByTimestampDesc(action);
        return logs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLogDTO> getLogsByDateRange(LocalDateTime start, LocalDateTime end) {
        List<AuditLogModel> logs = auditLogRepository.findByTimestampBetweenOrderByTimestampDesc(start, end);
        return logs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLogDTO> getRecentLogs(int limit) {
        List<AuditLogModel> logs = auditLogRepository.findTop100ByOrderByTimestampDesc();
        return logs.stream()
                .limit(limit)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public long getUserActionCount(Long userId) {
        return auditLogRepository.countByUser_UserId(userId);
    }

    private AuditLogDTO convertToDTO(AuditLogModel log) {
        AuditLogDTO dto = new AuditLogDTO();
        dto.setAuditId(log.getAuditId());

        if (log.getUser() != null) {
            dto.setUserId(log.getUser().getUserId());
            dto.setUserEmail(log.getUser().getEmail());
            dto.setUserName(log.getUser().getFirstName() + " " + log.getUser().getLastName());
        }

        dto.setAction(log.getAction());
        dto.setIpAddress(log.getIpAddress());
        dto.setUserAgent(log.getUserAgent());
        dto.setDetails(log.getDetails());
        dto.setTimestamp(log.getTimestamp());
        dto.setVerificationId(log.getVerificationId());
        dto.setSuccess(log.getSuccess());
        dto.setErrorMessage(log.getErrorMessage());

        return dto;
    }

    private String getClientIp(HttpServletRequest request) {
        if (request == null) {
            return "SYSTEM";
        }

        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip != null ? ip : "UNKNOWN";
    }

    private String getUserAgent(HttpServletRequest request) {
        if (request == null) {
            return "SYSTEM";
        }
        String userAgent = request.getHeader("User-Agent");
        return userAgent != null ? userAgent : "UNKNOWN";
    }
}