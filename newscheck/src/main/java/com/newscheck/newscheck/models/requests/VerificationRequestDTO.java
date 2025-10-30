package com.newscheck.newscheck.models.requests;

import com.newscheck.newscheck.models.enums.ContentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
    Developed by Group 6:
        Ken Aliling
        Anicia Kaela Bonayao
        Carl Norbi Felonia
        Cedrick Miguel Kaneko
        Dino Alfred T. Timbol (Group Leader)
 */

//Verification Request DTO class

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerificationRequestDTO {

    private Long userId;
    private ContentType contentType;
    private String contentText;
    private String contentUrl;
    private String imageBase64;

}