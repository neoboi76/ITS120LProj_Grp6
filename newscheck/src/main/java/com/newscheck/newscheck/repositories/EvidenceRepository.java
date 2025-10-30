package com.newscheck.newscheck.repositories;


import com.newscheck.newscheck.models.EvidenceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
    Developed by Group 6:
        Ken Aliling
        Anicia Kaela Bonayao
        Carl Norbi Felonia
        Cedrick Miguel Kaneko
        Dino Alfred T. Timbol (Group Leader)
 */

//Evidence repository class, extending JpaRepository, containing
//db methods related to the evidences table

@Repository
public interface EvidenceRepository extends JpaRepository<EvidenceModel, Long> {

    List<EvidenceModel> findByVerdict_VerdictId(Long verdictId);

}