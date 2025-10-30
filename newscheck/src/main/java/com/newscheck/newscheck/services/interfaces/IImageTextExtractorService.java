package com.newscheck.newscheck.services.interfaces;

/*
    Developed by Group 6:
        Ken Aliling
        Anicia Kaela Bonayao
        Carl Norbi Felonia
        Cedrick Miguel Kaneko
        Dino Alfred T. Timbol (Group Leader)
 */

//Interface for the image text extractor service. Promotes loose coupling
//and dependency injection

public interface IImageTextExtractorService {

    String extractTextFromImage(String base64Image) throws Exception;

}