package com.newscheck.newscheck.models;

import com.newscheck.newscheck.models.enums.AuditAction;
import jakarta.persistence.*;
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

//Audit log model class (entity)

@Entity
@Table(name = "audit_logs")//table name is audit_logs in db
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogModel {

    //PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long auditId;

    //has userId FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = true)
    private UserModel user;

    //Audit action (see AuditAction enum)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuditAction action;

    //Ip address
    @Column(nullable = false)
    private String ipAddress;

    //User agent (optional)
    private String userAgent;

    //Details of the audit log
    @Column(columnDefinition = "TEXT")
    private String details;

    //Timestamp of the log
    @Column(nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();

    //Verification id (optional FK)
    private Long verificationId;

    //Success boolean (optional
    private Boolean success = true;

    //Error message field
    @Column(columnDefinition = "TEXT")
    private String errorMessage;

}