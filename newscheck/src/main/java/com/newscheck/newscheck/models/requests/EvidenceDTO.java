package com.newscheck.newscheck.models.requests;

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

//Evidence request DTO class

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvidenceDTO {

    private Long evidenceId;
    private String sourceName;
    private String sourceUrl;
    private String description;
    private Double relevanceScore;

}
