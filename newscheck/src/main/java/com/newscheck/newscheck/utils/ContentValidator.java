package com.newscheck.newscheck.utils;

import com.newscheck.newscheck.models.requests.VerificationRequestDTO;
import com.newscheck.newscheck.models.enums.ContentType;

public class ContentValidator {

    public static void validateVerificationRequest(VerificationRequestDTO request) throws IllegalArgumentException {
        if (request.getUserId() == null) {
            throw new IllegalArgumentException("User ID is required");
        }

        if (request.getContentType() == null) {
            throw new IllegalArgumentException("Content type is required");
        }

        switch (request.getContentType()) {
            case TEXT:
                if (request.getContentText() == null || request.getContentText().trim().isEmpty()) {
                    throw new IllegalArgumentException("Content text is required for TEXT type");
                }
                if (request.getContentText().length() < 10) {
                    throw new IllegalArgumentException("Content text must be at least 10 characters long");
                }
                break;

            case URL:
                if (request.getContentUrl() == null || request.getContentUrl().trim().isEmpty()) {
                    throw new IllegalArgumentException("Content URL is required for URL type");
                }
                if (!isValidUrl(request.getContentUrl())) {
                    throw new IllegalArgumentException("Invalid URL format");
                }
                break;

            case IMAGE:
                if (request.getImageBase64() == null || request.getImageBase64().trim().isEmpty()) {
                    throw new IllegalArgumentException("Image data is required for IMAGE type");
                }
                break;

            default:
                throw new IllegalArgumentException("Unsupported content type");
        }
    }

    private static boolean isValidUrl(String url) {
        try {
            String urlPattern = "^(https?://)?(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)$";
            return url.matches(urlPattern);
        } catch (Exception e) {
            return false;
        }
    }

    public static String sanitizeContent(String content) {
        if (content == null) {
            return "";
        }

        return content.trim()
                .replaceAll("<script[^>]*>.*?</script>", "")
                .replaceAll("<[^>]+>", "") // Remove HTML tags
                .replaceAll("\\s+", " "); // Normalize whitespace
    }
}