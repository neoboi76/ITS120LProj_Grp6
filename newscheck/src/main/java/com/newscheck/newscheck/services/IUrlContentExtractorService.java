package com.newscheck.newscheck.services;

public interface IUrlContentExtractorService {

    String extractContent(String url) throws Exception;
    String extractTitle(String claim) throws Exception;
}