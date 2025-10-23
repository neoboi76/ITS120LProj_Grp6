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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "verdictId", nullable = false)
    private VerdictModel verdict;

    @Column(nullable = false, length = 500)
    private String sourceName;

    @Column(nullable = false, length = 1000)
    private String sourceUrl;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Double relevanceScore;


}
