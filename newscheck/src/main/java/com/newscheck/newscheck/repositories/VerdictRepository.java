package com.newscheck.newscheck.repositories;

import com.newscheck.newscheck.models.VerdictModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/*
    Developed by Group 6:
        Ken Aliling
        Anicia Kaela Bonayao
        Carl Norbi Felonia
        Cedrick Miguel Kaneko
        Dino Alfred T. Timbol (Group Leader)
 */

//Verdict repository class, extending JpaRepository, containing
//db methods related to the verdict table

@Repository
public interface VerdictRepository extends JpaRepository<VerdictModel, Long> {

    Optional<VerdictModel> findByVerification_VerificationId(Long verificationId);

}
