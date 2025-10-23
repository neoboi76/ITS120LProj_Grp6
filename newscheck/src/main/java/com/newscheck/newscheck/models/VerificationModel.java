package com.newscheck.newscheck.models;

import com.newscheck.newscheck.models.enums.ContentType;
import com.newscheck.newscheck.models.enums.VerificationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "verifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerificationModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long verificationId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId", nullable = false)
    private UserModel user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContentType contentType;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String contentText;

    private String contentUrl;

    private String imagePath;

    @Enumerated(EnumType.STRING)
    private VerificationStatus status;

    @Column(nullable = false)
    private LocalDateTime submittedAt = LocalDateTime.now();

    @OneToOne(mappedBy = "verification", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private VerdictModel verdict;

    private Double score;
}
