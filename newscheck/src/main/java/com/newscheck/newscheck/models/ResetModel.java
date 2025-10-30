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

//Reset password request DTO class

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResetModel {

    private String email;
    private String oldPassword;
    private String newPassword;
    private String token;

}
