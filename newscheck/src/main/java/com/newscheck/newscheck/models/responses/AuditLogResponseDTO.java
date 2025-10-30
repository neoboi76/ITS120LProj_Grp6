package com.newscheck.newscheck.models.responses;

import com.newscheck.newscheck.models.enums.AuditAction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/*
    Developed by Group 6:
        Ken Aliling
        Anicia Kaela Bonayao
        Carl Norbi Felonia
        Cedrick Miguel Kaneko
        Dino Alfred T. Timbol (Group Leader)
 */

//Audit log response DTO class

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditLogResponseDTO {

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