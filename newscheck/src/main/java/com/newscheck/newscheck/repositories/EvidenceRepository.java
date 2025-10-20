package com.newscheck.newscheck.repositories;


import com.newscheck.newscheck.models.EvidenceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvidenceRepository extends JpaRepository<EvidenceModel, Long> {

    List<EvidenceModel> findByVerdict_VerdictId(Long verdictId);

}