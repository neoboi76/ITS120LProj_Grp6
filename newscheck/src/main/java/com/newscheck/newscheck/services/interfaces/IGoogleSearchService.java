package com.newscheck.newscheck.services.interfaces;

import com.newscheck.newscheck.models.search.SearchResult;
import java.util.List;

/*
    Developed by Group 6:
        Ken Aliling
        Anicia Kaela Bonayao
        Carl Norbi Felonia
        Cedrick Miguel Kaneko
        Dino Alfred T. Timbol (Group Leader)
 */

//Interface for the google search service. Promotes loose coupling
//and dependency injection

public interface IGoogleSearchService {

    List<SearchResult> searchNews(String query, int maxResults) throws Exception;

}