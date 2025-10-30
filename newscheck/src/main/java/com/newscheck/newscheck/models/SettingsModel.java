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

//Settings request DTO class

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SettingsModel {
    private String firstName;
    private String lastName;
    private long id;
    private String gender;
    private String country;
    private String language;
}
