package com.newscheck.newscheck.services.implementations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newscheck.newscheck.models.Gemini.GeminiRequest;
import com.newscheck.newscheck.models.Gemini.GeminiResponse;
import com.newscheck.newscheck.services.interfaces.IImageTextExtractorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/*
    Developed by Group 6:
        Ken Aliling
        Anicia Kaela Bonayao
        Carl Norbi Felonia
        Cedrick Miguel Kaneko
        Dino Alfred T. Timbol (Group Leader)
 */

//Image text extractor service. Contains business logic
//for image text extraction operations

@Service
@RequiredArgsConstructor
public class ImageTextExtractorService implements IImageTextExtractorService {

    //References gemini api key stored in the
    //application.properties file
    @Value("${gemini.api.key}")
    private String apiKey;

    //References gemini model url stored in the
    //application.properties file
    @Value("${gemini.model.url}")
    private String modelUrl;

    //Used for building queries to and from Google Custom Search API
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    //Extracts text from image. Takes as input a base64 string.
    @Override
    public String extractTextFromImage(String base64Image) throws Exception {
        String url = modelUrl + "?key=" + apiKey;

        Map<String, Object> inlineData = new HashMap<>();
        inlineData.put("mimeType", "image/jpeg");
        inlineData.put("data", base64Image);

        Map<String, Object> imagePart = new HashMap<>();
        imagePart.put("inlineData", inlineData);

        Map<String, Object> textPart = new HashMap<>();
        textPart.put("text", "Extract and transcribe all text content from this image. " +
                "Include headlines, body text, captions, and any other visible text. " +
                "Preserve the structure and order as much as possible.");

        List<Map<String, Object>> parts = new ArrayList<>();
        parts.add(textPart);
        parts.add(imagePart);

        Map<String, Object> content = new HashMap<>();
        content.put("parts", parts);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("contents", List.of(content));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                String.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            GeminiResponse geminiResponse = objectMapper.readValue(
                    response.getBody(),
                    GeminiResponse.class
            );

            if (geminiResponse.getCandidates() != null &&
                    !geminiResponse.getCandidates().isEmpty()) {
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
