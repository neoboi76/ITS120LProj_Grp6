package com.newscheck.newscheck.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;

/*
    Developed by Group 6:
        Ken Aliling
        Anicia Kaela Bonayao
        Carl Norbi Felonia
        Cedrick Miguel Kaneko
        Dino Alfred T. Timbol (Group Leader)
 */

//Token blacklist model class (entity)
//Stores tokens that are blacklisted (when a user logs out and ends a session)

@Entity
@Table(name = "token_black_list")//name in db is token_black_list
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenBlackList {

    //PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //JWT Token that is blacklisted
    @Column(nullable = false, unique = true, length = 512)
    private String token;

    //Expiry Date
    @Column(nullable = false)
    private Instant expiryDate;


    public TokenBlackList(String token, Instant expiryDate) {

        this.token = token;
        this.expiryDate = expiryDate;

    }
}