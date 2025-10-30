package com.newscheck.newscheck.models.responses;

import com.newscheck.newscheck.models.enums.VerificationStatus;
import com.newscheck.newscheck.models.enums.VerdictType;
import com.newscheck.newscheck.models.requests.EvidenceDTO;
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

//Verification response DTO class

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerificationResponseDTO {

    private Long verificationId;
    private VerificationStatus status;
    private String claim;
    private VerdictType verdictType;
    private String reasoning;
    private Double confidenceScore;
    private List<EvidenceDTO> evidences;
    private LocalDateTime submittedAt;
    private LocalDateTime completedAt;
    private String message;

}