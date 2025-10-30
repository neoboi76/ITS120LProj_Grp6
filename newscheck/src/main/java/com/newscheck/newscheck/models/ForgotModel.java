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

//Forgot password request DTO class

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForgotModel {
    private String email;
    private String newPassword;
    private String token;
}
