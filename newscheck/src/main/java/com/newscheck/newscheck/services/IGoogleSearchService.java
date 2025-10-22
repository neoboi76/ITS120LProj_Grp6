package com.newscheck.newscheck.services;

import com.newscheck.newscheck.models.search.SearchResult;
import java.util.List;

public interface IGoogleSearchService {

    List<SearchResult> searchNews(String query, int maxResults) throws Exception;

}