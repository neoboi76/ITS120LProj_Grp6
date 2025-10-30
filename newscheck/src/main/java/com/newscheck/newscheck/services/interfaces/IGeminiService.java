package com.newscheck.newscheck.services.interfaces;

import com.newscheck.newscheck.models.Gemini.GeminiAnalysisResult;
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

//Interface for the auth service. Promotes loose coupling
//and dependency injection

public interface IGeminiService {

    GeminiAnalysisResult analyzeContent(String content) throws Exception;
    GeminiAnalysisResult analyzeContentWithSearch(String content, List<SearchResult> searchResults) throws Exception;
}