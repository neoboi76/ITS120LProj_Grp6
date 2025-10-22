package com.newscheck.newscheck.models.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchResult {

    private String title;
    private String link;
    private String snippet;
    private String displayLink;
    private String formattedUrl;

}