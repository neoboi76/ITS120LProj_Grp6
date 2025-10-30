package com.newscheck.newscheck.models.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
    Developed by Group 6:
        Ken Aliling
        Anicia Kaela Bonayao
        Carl Norbi Felonia
        Cedrick Miguel Kaneko
        Dino Alfred T. Timbol (Group Leader)
 */

//Search result DTO class

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