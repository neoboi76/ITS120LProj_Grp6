package com.newscheck.newscheck.models.Gemini;

import com.newscheck.newscheck.models.enums.VerdictType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeminiAnalysisResult {

    private VerdictType verdict;
    private String reasoning;
    private Double confidenceScore;
    private List<SourceEvidence> sources;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SourceEvidence {
        private String sourceName;
        private String sourceUrl;
        private String description;
        private Double relevanceScore;
    }

}