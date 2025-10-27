package com.newscheck.newscheck.models.requests;

import com.newscheck.newscheck.models.enums.AuditAction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditLogDTO {

    private Long auditId;
    private Long userId;
    private String userEmail;
    private String userName;
    private AuditAction action;
    private String ipAddress;
    private String userAgent;
    private String details;
    private LocalDateTime timestamp;
    private Long verificationId;
    private Boolean success;
    private String errorMessage;

}