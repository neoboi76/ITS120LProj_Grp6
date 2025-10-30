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

//Login response DTO class

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    private String message;
    private String token;
    private String email;
    private String firstName;
    private String lastName;
    private String gender;
    private String country;
    private String language;
    private String role;
    private long id;
}
