package com.newscheck.newscheck.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "evidences")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvidenceModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long evidenceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verdictId", nullable = false)
    private VerdictModel verdict;

    @Column(nullable = false)
    private String sourceName;

    @Column(nullable = false)
    private String sourceUrl;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Double relevanceScore;


}
