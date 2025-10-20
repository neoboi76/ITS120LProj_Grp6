package com.newscheck.newscheck.services;

public interface IImageTextExtractorService {

    String extractTextFromImage(String base64Image) throws Exception;

}