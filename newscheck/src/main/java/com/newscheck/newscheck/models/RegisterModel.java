package com.newscheck.newscheck.models;

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

//Register request DTO class

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterModel {

    private String firstName;
    private String LastName;
    private String email;
    private String password;

}
