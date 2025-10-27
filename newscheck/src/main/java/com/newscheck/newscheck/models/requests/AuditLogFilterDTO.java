package com.newscheck.newscheck.models.requests;

import com.newscheck.newscheck.models.enums.AuditAction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditLogFilterDTO {

    private Long userId;
    private AuditAction action;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String ipAddress;
    private Boolean success;

}