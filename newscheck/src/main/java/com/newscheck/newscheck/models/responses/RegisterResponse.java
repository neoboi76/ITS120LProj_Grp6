package com.newscheck.newscheck.models.responses;

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

//Register response DTO class

@Data
@AllArgsConstructor
@NoArgsConstructor

public class RegisterResponse {
    private String message;
    private String email;
    private Long id;
}
