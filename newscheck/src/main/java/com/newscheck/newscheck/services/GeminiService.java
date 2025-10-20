package com.newscheck.newscheck.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newscheck.newscheck.models.enums.VerdictType;
import com.newscheck.newscheck.models.Gemini.GeminiAnalysisResult;
import com.newscheck.newscheck.models.Gemini.GeminiRequest;
import com.newscheck.newscheck.models.Gemini.GeminiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GeminiService implements IGeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.model.url}")
    private String modelUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public GeminiAnalysisResult analyzeContent(String content) throws Exception {
        String prompt = buildAnalysisPrompt(content);
        String response = callGeminiAPI(prompt);
        return parseGeminiResponse(response);
    }

    private String buildAnalysisPrompt(String content) {
        return """
                Analyze the following news content and determine if it's TRUE, FALSE, or UNDEFINED (if there's not enough information to determine).
                Use REAL, EXISTING, and TRACEABLE sources from reputable news organizations or academic journals. If the 
                URL doesn't work or access blocked you for some reason, try to search on google about the article
                and infer content based on the content of the link. Then, utilizing both, construct your analysis 
                and retrieve your sources.
                
                Provide your analysis in the following JSON format ONLY (no additional text):
                {
                    "verdict": "TRUE" or "FALSE" or "UNDEFINED",
                    "reasoning": "Detailed explanation of your analysis",
                    "confidenceScore": 0.0 to 1.0,
                    "sources": [
                        {
                            "sourceName": "Name of the source",
                            "sourceUrl": "URL if available, otherwise 'N/A'",
                            "description": "Brief description of how this source supports the verdict",
                            "relevanceScore": 0.0 to 1.0
                        }
                    ]
                }
                
                Consider the following in your analysis:
                1. Factual accuracy
                2. Source credibility
                3. Logical consistency
                4. Context and framing
                5. Potential bias or misleading information
                6. Temporal relevance
                
                News Content:
                """ + content;
    }

    private String callGeminiAPI(String prompt) throws Exception {
        String url = modelUrl + "?key=" + apiKey;

        GeminiRequest.Part part = new GeminiRequest.Part(prompt);
        GeminiRequest.Content contentObj = new GeminiRequest.Content(Collections.singletonList(part));
        GeminiRequest request = new GeminiRequest(Collections.singletonList(contentObj));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<GeminiRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                String.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new Exception("Gemini API call failed with status: " + response.getStatusCode());
        }
    }

    private GeminiAnalysisResult parseGeminiResponse(String responseBody) throws Exception {
        GeminiResponse geminiResponse = objectMapper.readValue(responseBody, GeminiResponse.class);

        if (geminiResponse.getCandidates() == null || geminiResponse.getCandidates().isEmpty()) {
            throw new Exception("No candidates in Gemini response");
        }

        String aiText = geminiResponse.getCandidates().get(0)
                .getContent()
                .getParts()
                .get(0)
                .getText();

        String jsonText = extractJSON(aiText);

        JsonNode jsonNode = objectMapper.readTree(jsonText);

        GeminiAnalysisResult result = new GeminiAnalysisResult();
        result.setVerdict(VerdictType.valueOf(jsonNode.get("verdict").asText()));
        result.setReasoning(jsonNode.get("reasoning").asText());
        result.setConfidenceScore(jsonNode.get("confidenceScore").asDouble());

        List<GeminiAnalysisResult.SourceEvidence> sources = new ArrayList<>();
        if (jsonNode.has("sources") && jsonNode.get("sources").isArray()) {
            for (JsonNode sourceNode : jsonNode.get("sources")) {
                GeminiAnalysisResult.SourceEvidence evidence = new GeminiAnalysisResult.SourceEvidence();
                evidence.setSourceName(sourceNode.get("sourceName").asText());
                evidence.setSourceUrl(sourceNode.get("sourceUrl").asText());
                evidence.setDescription(sourceNode.get("description").asText());
                evidence.setRelevanceScore(sourceNode.get("relevanceScore").asDouble());
                sources.add(evidence);
            }
        }
        result.setSources(sources);

        return result;
    }

    private String extractJSON(String text) {
        text = text.trim();
        if (text.startsWith("```json")) {
            text = text.substring(7);
        } else if (text.startsWith("```")) {
            text = text.substring(3);
        }
        if (text.endsWith("```")) {
            text = text.substring(0, text.length() - 3);
        }
        return text.trim();
    }
}