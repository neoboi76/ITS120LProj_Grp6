package com.newscheck.newscheck.controllers;

import com.newscheck.newscheck.models.requests.AuditLogFilterDTO;
import com.newscheck.newscheck.models.responses.AuditLogResponseDTO;
import com.newscheck.newscheck.services.IAuditLogService;
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
@RequestMapping("/api/admin/audit-logs")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')") // Class-level: Only admins can access any endpoint here
public class AuditLogController {

    private final IAuditLogService auditLogService;

    /**
     * Get all audit logs with pagination and filtering
     * Only accessible by ADMIN role
     */
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

            // Build filter
            AuditLogFilterDTO filter = new AuditLogFilterDTO();
            filter.setUserId(userId);
            filter.setAction(action);

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

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Failed to retrieve audit logs: " + e.getMessage());
        }
    }

    /**
     * Get audit logs for a specific user
     * Only accessible by ADMIN role
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserAuditLogs(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        try {
            Pageable pageable = PageRequest.of(page, size,
                    Sort.by(Sort.Direction.DESC, "timestamp"));

            Page<AuditLogResponseDTO> auditLogs =
                    auditLogService.getAuditLogsByUserId(userId, pageable);

            Map<String, Object> response = new HashMap<>();
            response.put("content", auditLogs.getContent());
            response.put("currentPage", auditLogs.getNumber());
            response.put("totalItems", auditLogs.getTotalElements());
            response.put("totalPages", auditLogs.getTotalPages());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Failed to retrieve user audit logs: " + e.getMessage());
        }
    }

    /**
     * Get audit logs for a specific verification
     * Only accessible by ADMIN role
     */
    @GetMapping("/verification/{verificationId}")
    public ResponseEntity<?> getVerificationAuditLogs(@PathVariable Long verificationId) {
        try {
            var auditLogs = auditLogService.getAuditLogsByVerificationId(verificationId);
            return ResponseEntity.ok(auditLogs);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Failed to retrieve verification audit logs: " + e.getMessage());
        }
    }

    /**
     * Get audit log statistics/summary
     * Only accessible by ADMIN role
     */
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

    /**
     * Delete old audit logs (cleanup)
     * Only accessible by ADMIN role
     */
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

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Failed to cleanup audit logs: " + e.getMessage());
        }
    }
}