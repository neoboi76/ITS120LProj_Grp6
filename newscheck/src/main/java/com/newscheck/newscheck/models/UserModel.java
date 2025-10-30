package com.newscheck.newscheck.models;

import com.newscheck.newscheck.models.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

/*
    Developed by Group 6:
        Ken Aliling
        Anicia Kaela Bonayao
        Carl Norbi Felonia
        Cedrick Miguel Kaneko
        Dino Alfred T. Timbol (Group Leader)
 */

//User model class (entity)

@Entity
@Table(name = "users")//name in db is users
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {

    //PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;

    //has email/username
    @Column(nullable = false, unique = true)
    private String email;

    //Note: The db does not store the passwords in plaintext. Instead, they
    //are encrypted using the BCrypt hashing algorithm. Passwords are
    //stored in hashes, not plaintext.
    @Column(nullable = false)
    private String passwordHash;

    //User firstname
    @Column(nullable = false)
    private String firstName;

    //User lastname
    @Column(nullable = false)
    private String lastName;

    //Optional gender
    private String gender;

    //Optional country
    private String country;

    //Optional language
    private String language;

    //Role can only be USER or ADMIN. By default,
    //it is set to USER
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;

    //Stores timestamp of date & time of user creation
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    //Related to the Verifications table (entity)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<VerificationModel> verifications;

    //Related to the audit logs table (entity)
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<AuditLogModel> auditLogs;

    //Related to the password reset tokens table
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<PasswordResetToken> resetTokens;


}

