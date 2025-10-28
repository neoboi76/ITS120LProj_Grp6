package com.newscheck.newscheck.services.implementations;

import com.newscheck.newscheck.models.AuditLogModel;
import com.newscheck.newscheck.models.UserModel;
import com.newscheck.newscheck.models.VerificationModel;
import com.newscheck.newscheck.models.enums.AuditAction;
import com.newscheck.newscheck.models.requests.AuditLogDTO;
import com.newscheck.newscheck.models.requests.AuditLogFilterDTO;
import com.newscheck.newscheck.models.responses.AuditLogResponseDTO;
import com.newscheck.newscheck.repositories.AuditLogRepository;
import com.newscheck.newscheck.repositories.UserRepository;
import com.newscheck.newscheck.repositories.VerificationRepository;
import com.newscheck.newscheck.services.interfaces.IAuditLogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuditLogService implements IAuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;
    private final VerificationRepository verificationRepository;

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
    public void verLog(AuditAction action, Long verificationId, String details, HttpServletRequest request) {
        VerificationModel verification = null;
        UserModel user = null;
        Long userId = verificationRepository.getUserIdByVerificationId(verificationId);

        if (userId != null) {
            user = userRepository.findById(userId).orElse(null);
        }

        if (verificationId != null) {
            verification = verificationRepository.findById(verificationId).orElse(null);
        }

        log(action, user, details, verificationId, request);
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

    @Override
    @Transactional(readOnly = true)
    public Page<AuditLogResponseDTO> getAllAuditLogs(Pageable pageable, AuditLogFilterDTO filter) {
        Page<AuditLogModel> logs;

        if (filter != null && hasFilters(filter)) {
            logs = auditLogRepository.findAll(new AuditLogSpecification(filter), pageable);
        } else {
            logs = auditLogRepository.findAll(pageable);
        }

        return logs.map(this::convertToResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuditLogResponseDTO> getAuditLogsByUserId(Long userId, Pageable pageable) {
        Page<AuditLogModel> logs = auditLogRepository.findByUser_UserIdOrderByTimestampDesc(userId, pageable);
        return logs.map(this::convertToResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuditLogResponseDTO> getAuditLogsByVerificationId(Long verificationId, Pageable pageable) {
        Page<AuditLogModel> logs = auditLogRepository.findByVerificationIdOrderByTimestampDesc(verificationId, pageable);
        return logs.map(this::convertToResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getAuditLogStatistics() {
        Map<String, Object> stats = new HashMap<>();

        stats.put("totalLogs", auditLogRepository.count());
        stats.put("totalUsers", userRepository.count());

        Map<String, Long> actionCounts = new HashMap<>();
        for (AuditAction action : AuditAction.values()) {
            long count = auditLogRepository.countByAction(action);
            if (count > 0) {
                actionCounts.put(action.name(), count);
            }
        }
        stats.put("actionCounts", actionCounts);

        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        List<AuditLogModel> recentLogs = auditLogRepository.findByTimestampBetweenOrderByTimestampDesc(
                yesterday, LocalDateTime.now());
        stats.put("logsLast24Hours", recentLogs.size());

        return stats;
    }

    @Override
    @Transactional
    public long deleteAuditLogsOlderThan(LocalDateTime cutoffDate) {
        List<AuditLogModel> oldLogs = auditLogRepository.findByTimestampBefore(cutoffDate);
        long count = oldLogs.size();
        auditLogRepository.deleteAll(oldLogs);
        return count;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getAllUsersWithLogCounts() {
        List<Map<String, Object>> userLogData = new ArrayList<>();

        List<UserModel> allUsers = userRepository.findAll();

        for (UserModel user : allUsers) {
            Map<String, Object> userData = new HashMap<>();
            userData.put("userId", user.getUserId());
            userData.put("email", user.getEmail());
            userData.put("firstName", user.getFirstName());
            userData.put("lastName", user.getLastName());
            userData.put("fullName", user.getFirstName() + " " + user.getLastName());
            userData.put("role", user.getRole());
            userData.put("createdAt", user.getCreatedAt());

            long logCount = auditLogRepository.countByUser_UserId(user.getUserId());
            userData.put("auditLogCount", logCount);

            List<AuditLogModel> recentLogs = auditLogRepository.findByUser_UserIdOrderByTimestampDesc(user.getUserId())
                    .stream()
                    .limit(1)
                    .collect(Collectors.toList());

            if (!recentLogs.isEmpty()) {
                userData.put("lastActivity", recentLogs.get(0).getTimestamp());
                userData.put("lastAction", recentLogs.get(0).getAction());
            } else {
                userData.put("lastActivity", null);
                userData.put("lastAction", null);
            }

            userLogData.add(userData);
        }

        return userLogData;
    }

    private boolean hasFilters(AuditLogFilterDTO filter) {
        return filter.getUserId() != null ||
                filter.getAction() != null ||
                filter.getStartDate() != null ||
                filter.getEndDate() != null ||
                filter.getIpAddress() != null ||
                filter.getSuccess() != null;
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

    private AuditLogResponseDTO convertToResponseDTO(AuditLogModel log) {
        AuditLogResponseDTO dto = new AuditLogResponseDTO();
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