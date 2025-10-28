package com.newscheck.newscheck.services.implementations;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newscheck.newscheck.models.search.SearchResult;
import com.newscheck.newscheck.services.interfaces.IGoogleSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoogleSearchService implements IGoogleSearchService {

    @Value("${google.search.api.key}")
    private String apiKey;

    @Value("${google.search.engine.id}")
    private String searchEngineId;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<SearchResult> searchNews(String query, int maxResults) throws Exception {
        try {

            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);


            String url = String.format(
                    "https://www.googleapis.com/customsearch/v1?key=%s&cx=%s&q=%s&num=%d",
                    apiKey,
                    searchEngineId,
                    encodedQuery,
                    Math.min(maxResults, 10)
            );


            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                return parseSearchResults(response.getBody());
            } else {
                throw new Exception("Google Search API call failed: " + response.getStatusCode());
            }

        } catch (Exception e) {
            throw new Exception("Failed to perform Google search: " + e.getMessage());
        }
    }

    private List<SearchResult> parseSearchResults(String responseBody) throws Exception {
        List<SearchResult> results = new ArrayList<>();

        JsonNode root = objectMapper.readTree(responseBody);
        JsonNode items = root.get("items");

        if (items != null && items.isArray()) {
            for (JsonNode item : items) {
                SearchResult result = new SearchResult();
                result.setTitle(item.get("title").asText());
                result.setLink(item.get("link").asText());
                result.setSnippet(item.get("snippet").asText());
                result.setDisplayLink(item.has("displayLink") ? item.get("displayLink").asText() : "");
                result.setFormattedUrl(item.has("formattedUrl") ? item.get("formattedUrl").asText() : "");
                results.add(result);
            }
        }

        return results;
    }
}