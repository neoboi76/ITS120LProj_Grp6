package com.newscheck.newscheck.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
    Developed by Group 6:
        Ken Aliling
        Anicia Kaela Bonayao
        Carl Norbi Felonia
        Cedrick Miguel Kaneko
        Dino Alfred T. Timbol (Group Leader)
 */

//Evidence Model class (entity)

@Entity
@Table(name = "evidences")//table name is evidences in db
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvidenceModel {

    //PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long evidenceId;

    //has verdictID FK
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "verdictId", nullable = false)
    private VerdictModel verdict;

    //Source name
    @Column(nullable = false, length = 500)
    private String sourceName;

    //Source url if contentType == URL
    @Column(nullable = false, length = 1000)
    private String sourceUrl;

    //Source description (optional)
    @Column(columnDefinition = "TEXT")
    private String description;

    //Relevance score (optional)
    private Double relevanceScore;


}
