package com.newscheck.newscheck.models.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
