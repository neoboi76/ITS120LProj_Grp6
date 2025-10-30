package com.newscheck.newscheck.models.Gemini;

import com.newscheck.newscheck.models.enums.VerdictType;
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

//Gemini 2.5 Pro API analysis result DTO

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeminiAnalysisResult {

    private VerdictType verdict;
    private String reasoning;
    private Double confidenceScore;
    private List<SourceEvidence> sources;

    @Data //Array of sources
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SourceEvidence {
        private String sourceName;
        private String sourceUrl;
        private String description;
        private Double relevanceScore;
    }

}