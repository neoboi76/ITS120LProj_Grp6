package com.newscheck.newscheck.services;


import com.newscheck.newscheck.models.Gemini.GeminiAnalysisResult;

public interface IGeminiService {

    GeminiAnalysisResult analyzeContent(String content) throws Exception;

}
