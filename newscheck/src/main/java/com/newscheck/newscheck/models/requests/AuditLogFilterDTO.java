package com.newscheck.newscheck.models.requests;

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

//Audit log filter DTO class

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditLogFilterDTO {

    private Long userId;
    private Long verificationId;
    private AuditAction action;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String ipAddress;
    private Boolean success;

}