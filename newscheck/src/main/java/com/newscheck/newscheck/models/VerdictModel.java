package com.newscheck.newscheck.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "verdicts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerdictModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long verdictId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VerdictType verdictType;

    @OneToOne
    @JoinColumn(name = "verificationId")
    private VerificationModel verification;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String reasoning;

    private LocalDateTime verdictDate = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "verdict", cascade = CascadeType.ALL)
    private List<EvidenceModel> evidences;

}
