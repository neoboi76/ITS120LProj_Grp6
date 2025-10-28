package com.newscheck.newscheck.controllers;

import com.newscheck.newscheck.models.requests.AuditLogFilterDTO;
import com.newscheck.newscheck.models.responses.AuditLogResponseDTO;
import com.newscheck.newscheck.services.interfaces.IAuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/admin/audit-logs")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AuditLogController {

    private final IAuditLogService auditLogService;

    @GetMapping
    public ResponseEntity<?> getAllAuditLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "timestamp") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String action,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate
    ) {
        try {
            Sort.Direction direction = sortDirection.equalsIgnoreCase("ASC")
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC;

            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

            AuditLogFilterDTO filter = new AuditLogFilterDTO();
            filter.setUserId(userId);
            filter.setAction(action != null ? com.newscheck.newscheck.models.enums.AuditAction.valueOf(action) : null);

            if (startDate != null && !startDate.isEmpty()) {
                filter.setStartDate(LocalDateTime.parse(startDate));
            }
            if (endDate != null && !endDate.isEmpty()) {
                filter.setEndDate(LocalDateTime.parse(endDate));
            }

            Page<AuditLogResponseDTO> auditLogs = auditLogService.getAllAuditLogs(pageable, filter);

            Map<String, Object> response = new HashMap<>();
            response.put("content", auditLogs.getContent());
            response.put("currentPage", auditLogs.getNumber());
            response.put("totalItems", auditLogs.getTotalElements());
            response.put("totalPages", auditLogs.getTotalPages());
            response.put("pageSize", auditLogs.getSize());
            response.put("hasNext", auditLogs.hasNext());
            response.put("hasPrevious", auditLogs.hasPrevious());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Failed to retrieve audit logs: " + e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserAuditLogs(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "timestamp") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection
    ) {
        try {
            Sort.Direction direction = sortDirection.equalsIgnoreCase("ASC")
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC;

            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

            Page<AuditLogResponseDTO> auditLogs =
                    auditLogService.getAuditLogsByUserId(userId, pageable);

            Map<String, Object> response = new HashMap<>();
            response.put("content", auditLogs.getContent());
            response.put("currentPage", auditLogs.getNumber());
            response.put("totalItems", auditLogs.getTotalElements());
            response.put("totalPages", auditLogs.getTotalPages());
            response.put("pageSize", auditLogs.getSize());
            response.put("hasNext", auditLogs.hasNext());
            response.put("hasPrevious", auditLogs.hasPrevious());
            response.put("userId", userId);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Failed to retrieve user audit logs: " + e.getMessage());
        }
    }

    @GetMapping("/verification/{verificationId}")
    public ResponseEntity<?> getVerificationAuditLogs(
            @PathVariable Long verificationId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "timestamp") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection
    ) {
        try {
            Sort.Direction direction = sortDirection.equalsIgnoreCase("ASC")
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC;

            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

            Page<AuditLogResponseDTO> auditLogs =
                    auditLogService.getAuditLogsByVerificationId(verificationId, pageable);

            Map<String, Object> response = new HashMap<>();
            response.put("content", auditLogs.getContent());
            response.put("currentPage", auditLogs.getNumber());
            response.put("totalItems", auditLogs.getTotalElements());
            response.put("totalPages", auditLogs.getTotalPages());
            response.put("pageSize", auditLogs.getSize());
            response.put("hasNext", auditLogs.hasNext());
            response.put("hasPrevious", auditLogs.hasPrevious());
            response.put("verificationId", verificationId);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Failed to retrieve verification audit logs: " + e.getMessage());
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getAuditLogStats() {
        try {
            Map<String, Object> stats = auditLogService.getAuditLogStatistics();
            return ResponseEntity.ok(stats);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Failed to retrieve audit log statistics: " + e.getMessage());
        }
    }

    @DeleteMapping("/cleanup")
    public ResponseEntity<?> cleanupOldAuditLogs(
            @RequestParam(defaultValue = "90") int daysOld
    ) {
        try {
            LocalDateTime cutoffDate = LocalDateTime.now().minusDays(daysOld);
            long deletedCount = auditLogService.deleteAuditLogsOlderThan(cutoffDate);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Audit logs cleaned up successfully");
            response.put("deletedCount", deletedCount);
            response.put("cutoffDate", cutoffDate);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Failed to cleanup audit logs: " + e.getMessage());
        }
    }
}