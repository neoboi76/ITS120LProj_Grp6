package com.newscheck.newscheck.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
public class UrlContentExtractorService implements IUrlContentExtractorService {

    @Override
    public String extractContent(String url) throws Exception {
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .timeout(10000)
                    .get();

            StringBuilder content = new StringBuilder();

            String title = doc.title();
            if (title != null && !title.isEmpty()) {
                content.append("Title: ").append(title).append("\n\n");
            }

            Element metaDescription = doc.selectFirst("meta[name=description]");
            if (metaDescription != null) {
                String description = metaDescription.attr("content");
                if (!description.isEmpty()) {
                    content.append("Description: ").append(description).append("\n\n");
                }
            }

            Elements articleContent = doc.select("article, .article-content, .post-content, .entry-content, main");

            if (!articleContent.isEmpty()) {
                content.append("Content:\n");
                for (Element element : articleContent) {
                    // Get text from paragraphs
                    Elements paragraphs = element.select("p");
                    for (Element p : paragraphs) {
                        String text = p.text().trim();
                        if (!text.isEmpty() && text.length() > 30) { // Filter out short snippets
                            content.append(text).append("\n\n");
                        }
                    }
                }
            } else {
                Elements paragraphs = doc.select("p");
                content.append("Content:\n");
                for (Element p : paragraphs) {
                    String text = p.text().trim();
                    if (!text.isEmpty() && text.length() > 30) {
                        content.append(text).append("\n\n");
                    }
                }
            }

            String extractedContent = content.toString().trim();

            if (extractedContent.isEmpty()) {
                throw new Exception("Could not extract meaningful content from the URL");
            }

            if (extractedContent.length() > 10000) {
                extractedContent = extractedContent.substring(0, 10000) + "...";
            }

            return extractedContent;

        } catch (Exception e) {
            throw new Exception("Failed to extract content from URL: " + e.getMessage());
        }
    }
}