package com.newscheck.newscheck.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newscheck.newscheck.models.Gemini.GeminiRequest;
import com.newscheck.newscheck.models.Gemini.GeminiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        GeminiRequest.Part textPart = new GeminiRequest.Part(
                "Extract and transcribe all text content from this image. " +
                        "Include headlines, body text, captions, and any other visible text. " +
                        "Preserve the structure and order as much as possible."
        );

        Map<String, Object> inlineData = new HashMap<>();
        inlineData.put("mimeType", "image/jpeg");
        inlineData.put("data", base64Image);

        GeminiRequest.Part imagePart = new GeminiRequest.Part(
                objectMapper.writeValueAsString(Collections.singletonMap("inlineData", inlineData))
        );

        GeminiRequest.Content content = new GeminiRequest.Content(List.of(textPart, imagePart));
        GeminiRequest request = new GeminiRequest(List.of(content));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<GeminiRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

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
