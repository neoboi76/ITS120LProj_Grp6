package com.newscheck.newscheck.repositories;


import com.newscheck.newscheck.models.VerificationModel;
import com.newscheck.newscheck.models.enums.VerificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VerificationRepository extends JpaRepository<VerificationModel, Long> {

    List<VerificationModel> findByUser_UserId(Long userId);
    List<VerificationModel> findByStatus(VerificationStatus status);
    List<VerificationModel> findByUser_UserIdOrderBySubmittedAtDesc(Long userId);

}
