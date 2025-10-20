package com.newscheck.newscheck.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newscheck.newscheck.models.Gemini.GeminiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ImageTextExtractorService implements IImageTextExtractorService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.model.url}")
    private String modelUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String extractTextFromImage(String base64Image) throws Exception {
        String url = modelUrl + "?key=" + apiKey;

        // Build the request with image
        Map<String, Object> request = new HashMap<>();
        List<Map<String, Object>> contents = new ArrayList<>();
        Map<String, Object> content = new HashMap<>();
        List<Map<String, Object>> parts = new ArrayList<>();

        // Add text prompt
        Map<String, Object> textPart = new HashMap<>();
        textPart.put("text", "Extract and transcribe all text content from this image. Include headlines, body text, captions, and any other visible text. Preserve the structure and order as much as possible.");
        parts.add(textPart);

        // Add image
        Map<String, Object> imagePart = new HashMap<>();
        Map<String, String> inlineData = new HashMap<>();
        inlineData.put("mimeType", "image/jpeg"); // Adjust if needed
        inlineData.put("data", base64Image);
        imagePart.put("inlineData", inlineData);
        parts.add(imagePart);

        content.put("parts", parts);
        contents.add(content);
        request.put("contents", contents);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                String.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            GeminiResponse geminiResponse = objectMapper.readValue(response.getBody(), GeminiResponse.class);

            if (geminiResponse.getCandidates() != null && !geminiResponse.getCandidates().isEmpty()) {
                return geminiResponse.getCandidates().get(0)
                        .getContent()
                        .getParts()
                        .get(0)
                        .getText();
            } else {
                throw new Exception("No text extracted from image");
            }
        } else {
            throw new Exception("Failed to extract text from image: " + response.getStatusCode());
        }
    }
}