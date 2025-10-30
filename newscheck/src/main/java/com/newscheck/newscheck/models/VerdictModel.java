package com.newscheck.newscheck.models;

import com.newscheck.newscheck.models.enums.VerdictType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

//Verdict model class (entity)

@Entity
@Table(name = "verdicts")//name in the db is verdicts
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerdictModel {

    //PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long verdictId;

    //Verdicts can be TRUE, FALSE, or UNDEFINED
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VerdictType verdictType;

    //FK
    @OneToOne
    @JoinColumn(name = "verificationId")
    private VerificationModel verification;

    //Reasoning stored as LOB text
    @Lob
    @Column(columnDefinition = "TEXT")
    private String reasoning;

    //Date of the creation of the verdict
    private LocalDateTime verdictDate = LocalDateTime.now();

    //Timestamp of the verdict creation
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    //Has a relation to the evidences table (entity)
    @OneToMany(mappedBy = "verdict", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<EvidenceModel> evidences;

}
