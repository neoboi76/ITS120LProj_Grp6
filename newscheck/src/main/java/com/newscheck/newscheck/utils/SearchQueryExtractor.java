package com.newscheck.newscheck.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchQueryExtractor {


    public static String extractSearchQuery(String content) {
        if (content == null || content.isEmpty()) {
            return "";
        }

        String query = extractHeadline(content);

        query = cleanQuery(query);

        query = limitQueryLength(query, 100);

        return query;
    }

    private static String extractHeadline(String content) {
        Pattern titlePattern = Pattern.compile("(?i)(title|headline):\\s*(.+?)(?:\\n|\\.|$)");
        Matcher matcher = titlePattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(2).trim();
        }

        String[] lines = content.split("\\n");
        for (String line : lines) {
            line = line.trim();
            if (line.length() > 20 && !line.toLowerCase().startsWith("http")) {
                int periodIndex = line.indexOf('.');
                if (periodIndex > 0) {
                    return line.substring(0, periodIndex).trim();
                }
                return line;
            }
        }

        return content.substring(0, Math.min(150, content.length())).trim();
    }

    private static String cleanQuery(String query) {

        query = query.replaceAll("https?://\\S+", "");

        query = query.replaceAll("[!?]{2,}", "");

        query = query.replaceAll("[\"']", "");

        query = query.replaceAll("(?i)^(breaking|update|watch|read|click here|new|latest|just in):\\s*", "");

        query = query.replaceAll("\\s+", " ").trim();

        return query;
    }

    private static String limitQueryLength(String query, int maxChars) {
        if (query.length() <= maxChars) {
            return query;
        }

        int lastSpace = query.lastIndexOf(' ', maxChars);
        if (lastSpace > maxChars / 2) {
            return query.substring(0, lastSpace).trim();
        }

        return query.substring(0, maxChars).trim();
    }

    public static String enhanceQuery(String query) {
        // Add "news" to help filter out non-news results
        if (!query.toLowerCase().contains("news")) {
            query = query + " news";
        }
        return query;
    }
}