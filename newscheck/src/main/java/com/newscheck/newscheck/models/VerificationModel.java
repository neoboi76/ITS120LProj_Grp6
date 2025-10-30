package com.newscheck.newscheck.models;

import com.newscheck.newscheck.models.enums.ContentType;
import com.newscheck.newscheck.models.enums.VerificationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/*
    Developed by Group 6:
        Ken Aliling
        Anicia Kaela Bonayao
        Carl Norbi Felonia
        Cedrick Miguel Kaneko
        Dino Alfred T. Timbol (Group Leader)
 */

//Verifications model class (entity)

@Entity
@Table(name = "verifications")//table name in db is verification
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerificationModel {

    //PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long verificationId;

    //FK
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId", nullable = false)
    private UserModel user;

    //ContentType can be URL, IMAGE, or TEXT
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContentType contentType;

    //ContentText stored as LOB
    @Lob
    @Column(columnDefinition = "TEXT")
    private String contentText;

    //ContentUrl is optional
    private String contentUrl;

    //The base64 string of the image is stored as a LOB
    @Lob
    @Column(columnDefinition = "TEXT")
    private String imageBase64;

    //Verification status can be VERIFIED, PENDING, or, FAILED
    @Enumerated(EnumType.STRING)
    private VerificationStatus status;

    //Contains timestamp of submissio
    @Column(nullable = false)
    private LocalDateTime submittedAt = LocalDateTime.now();

    //Has a relation to the verdict table (entity)
    @OneToOne(mappedBy = "verification", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private VerdictModel verdict;

    //Holds score for the accuracy of the verifcation process
    private Double score;
}
