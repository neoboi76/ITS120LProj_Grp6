package com.newscheck.newscheck.services.implementations;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newscheck.newscheck.models.enums.VerdictType;
import com.newscheck.newscheck.models.Gemini.GeminiAnalysisResult;
import com.newscheck.newscheck.models.Gemini.GeminiRequest;
import com.newscheck.newscheck.models.Gemini.GeminiResponse;
import com.newscheck.newscheck.models.search.SearchResult;
import com.newscheck.newscheck.services.interfaces.IGeminiService;
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

    @Override
    public GeminiAnalysisResult analyzeContentWithSearch(String content, List<SearchResult> searchResults) throws Exception {
        String prompt = buildAnalysisPromptWithSearch(content, searchResults);
        String response = callGeminiAPI(prompt);
        return parseGeminiResponse(response);
    }

    private String buildAnalysisPrompt(String content) {

        return """
                You are a fact-checking assistant. Analyze the following news content and determine if it's likely TRUE, FALSE, or UNDEFINED.
                
                IMPORTANT LIMITATIONS:
                - You do NOT have access to real-time internet or current news
                - Base your analysis on logical consistency, known facts, and content patterns
                - Be HONEST about uncertainty - use UNDEFINED when you cannot verify with confidence
                
                Provide your analysis in the following JSON format ONLY (no additional text):
                {
                    "verdict": "TRUE" or "FALSE" or "UNDEFINED",
                    "reasoning": "Detailed explanation of your analysis. If uncertain, explain why.",
                    "confidenceScore": 0.0 to 1.0,
                    "sources": [
                        {
                            "sourceName": "Type of analysis (e.g., 'Logical Consistency Check', 'Pattern Analysis')",
                            "sourceUrl": "N/A",
                            "description": "Explanation of the analysis method used",
                            "relevanceScore": 0.0 to 1.0
                        }
                    ]
                }
                
                Analysis criteria:
                1. Logical consistency
                2. Known historical facts
                3. Language patterns (sensationalism, vagueness, clickbait)
                4. Plausibility based on your training knowledge
                5. For recent/current events: Use UNDEFINED and explain verification needs real-time sources
                
                News Content:
                """ + content;
    }

    private String buildAnalysisPromptWithSearch(String content, List<SearchResult> searchResults) {

        StringBuilder prompt = new StringBuilder();
        prompt.append("""
                You are a fact-checking assistant with access to real Google search results.
                Analyze the following news content and determine if it's TRUE, FALSE, or UNDEFINED.
                
                You have been provided with REAL, CURRENT Google search results related to this content.
                Use these search results as evidence to support your verdict. DO NOT USE WIKIPEDIA SOURCES AND/OR 
                REDDIT SOURCES, AND SOCIAL MEDIA SOURCES.! Use sources that come from accredited news organizations.
                If you are going to use social media sources, make sure it comes from verified news outlets, and not
                random Facebook, X, or Instagram posts! 
                Furthermore, you are a AI model that was trained using past data, but dealing with
                up-to-date, recent sources. From your perspective, they are the "future." However, if you have met with
                a claim that is about a "future" event from your perspective, but which to the perspective of the sender
                of this prompt, is the "present" and even it's past. Speak as if your "future" is in your "present" and/or
                past as reported by the sources. 
                
                Provide your analysis in the following JSON format ONLY (no additional text):
                {
                    "verdict": "TRUE" or "FALSE" or "UNDEFINED",
                    "reasoning": "Detailed explanation based on the search results provided",
                    "confidenceScore": 0.0 to 1.0,
                    "sources": [
                        {
                            "sourceName": "Exact title from search results",
                            "sourceUrl": "Exact URL from search results",
                            "description": "How this source supports or contradicts the claim",
                            "relevanceScore": 0.0 to 1.0
                        }
                    ]
                }
                
                Analysis Guidelines:
                1. Cross-reference the content with the search results
                2. Check if multiple reputable sources confirm the information
                3. Look for contradictions between the content and search results
                4. Evaluate source credibility (e.g., reuters.com, bbc.com vs unknown sites)
                5. Check publication dates - recent sources are more reliable for current events
                6. If search results don't provide enough information, use UNDEFINED
                
                IMPORTANT: Only cite sources that are actually provided in the search results below.
                Do NOT make up or hallucinate sources.
                
                ---GOOGLE SEARCH RESULTS---
                """);

        for (int i = 0; i < searchResults.size(); i++) {
            SearchResult result = searchResults.get(i);
            prompt.append(String.format("""
                    
                    Result %d:
                    Title: %s
                    URL: %s
                    Snippet: %s
                    Source: %s
                    """,
                    i + 1,
                    result.getTitle(),
                    result.getLink(),
                    result.getSnippet(),
                    result.getDisplayLink()
            ));
        }

        prompt.append("""
                
                ---END OF SEARCH RESULTS---
                
                Now analyze this news content:
                """);
        prompt.append(content);

        return prompt.toString();
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