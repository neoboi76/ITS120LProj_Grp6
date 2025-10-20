package com.newscheck.newscheck.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
