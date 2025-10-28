package com.newscheck.newscheck.services.interfaces;

public interface IImageTextExtractorService {

    String extractTextFromImage(String base64Image) throws Exception;

}