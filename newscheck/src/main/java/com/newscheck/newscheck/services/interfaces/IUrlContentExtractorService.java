package com.newscheck.newscheck.services.interfaces;

/*
    Developed by Group 6:
        Ken Aliling
        Anicia Kaela Bonayao
        Carl Norbi Felonia
        Cedrick Miguel Kaneko
        Dino Alfred T. Timbol (Group Leader)
 */

//Interface for the url content extractor service. Promotes loose coupling
//and dependency injection

public interface IUrlContentExtractorService {

    String extractContent(String url) throws Exception;
    String extractTitle(String claim) throws Exception;
}