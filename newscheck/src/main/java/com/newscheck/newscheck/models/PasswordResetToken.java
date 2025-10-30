package com.newscheck.newscheck.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/*
    Developed by Group 6:
        Ken Aliling
        Anicia Kaela Bonayao
        Carl Norbi Felonia
        Cedrick Miguel Kaneko
        Dino Alfred T. Timbol (Group Leader)
 */

//Password reset token model class (entity)

@Entity
@Table(name = "password_reset_tokens")//table name in db is password_reset_tokens
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetToken {

    //PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Token (optional)
    private String token;

    //FK
    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private UserModel user;

    //Date of expiry (optional)
    private LocalDateTime expiry;

}
