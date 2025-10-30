package com.newscheck.newscheck.repositories;

import com.newscheck.newscheck.models.AuditLogModel;
import com.newscheck.newscheck.models.enums.AuditAction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/*
    Developed by Group 6:
        Ken Aliling
        Anicia Kaela Bonayao
        Carl Norbi Felonia
        Cedrick Miguel Kaneko
        Dino Alfred T. Timbol (Group Leader)
 */

//Audit log repository class, extending JpaRepository, containing
//db methods related to the audit logs table

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLogModel, Long>, JpaSpecificationExecutor<AuditLogModel> {

    List<AuditLogModel> findByUser_UserIdOrderByTimestampDesc(Long userId);

    Page<AuditLogModel> findByUser_UserIdOrderByTimestampDesc(Long userId, Pageable pageable);

    List<AuditLogModel> findByActionOrderByTimestampDesc(AuditAction action);

    List<AuditLogModel> findByTimestampBetweenOrderByTimestampDesc(LocalDateTime start, LocalDateTime end);

    List<AuditLogModel> findByUser_UserIdAndActionOrderByTimestampDesc(Long userId, AuditAction action);

    List<AuditLogModel> findTop100ByOrderByTimestampDesc();

    long countByUser_UserId(Long userId);

    long countByAction(AuditAction action);

    List<AuditLogModel> findByVerificationIdOrderByTimestampDesc(Long verificationId);

    Page<AuditLogModel> findByVerificationIdOrderByTimestampDesc(Long verificationId, Pageable pageable);

    List<AuditLogModel> findByTimestampBefore(LocalDateTime cutoffDate);

    long countByTimestampAfter(LocalDateTime date);
}