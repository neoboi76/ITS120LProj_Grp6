package com.newscheck.newscheck.repositories;

import com.newscheck.newscheck.models.VerificationModel;
import com.newscheck.newscheck.models.enums.VerificationStatus;
import com.newscheck.newscheck.models.enums.VerdictType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VerificationRepository extends JpaRepository<VerificationModel, Long>, JpaSpecificationExecutor<VerificationModel> {

    List<VerificationModel> findByUser_UserId(Long userId);

    List<VerificationModel> findByStatus(VerificationStatus status);

    List<VerificationModel> findByUser_UserIdOrderBySubmittedAtDesc(Long userId);

    VerificationModel findByVerificationId(Long verificationId);

    long countByUser_UserId(Long userId);

    long countByStatus(VerificationStatus status);

    long countByVerdict_VerdictType(VerdictType verdictType);

    long countBySubmittedAtAfter(LocalDateTime date);

    @Query("SELECT v.user.userId FROM VerificationModel v WHERE v.verificationId = :verificationId")
    Long getUserIdByVerificationId(@Param("verificationId") Long verificationId);

}