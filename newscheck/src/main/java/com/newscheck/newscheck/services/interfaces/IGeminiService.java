package com.newscheck.newscheck.services.interfaces;

import com.newscheck.newscheck.models.Gemini.GeminiAnalysisResult;
import com.newscheck.newscheck.models.search.SearchResult;

import java.util.List;

public interface IGeminiService {

    GeminiAnalysisResult analyzeContent(String content) throws Exception;
    GeminiAnalysisResult analyzeContentWithSearch(String content, List<SearchResult> searchResults) throws Exception;
}