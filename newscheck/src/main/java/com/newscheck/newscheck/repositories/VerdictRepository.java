package com.newscheck.newscheck.repositories;

import com.newscheck.newscheck.models.VerdictModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerdictRepository extends JpaRepository<VerdictModel, Long> {

    Optional<VerdictModel> findByVerification_VerificationId(Long verificationId);

}
