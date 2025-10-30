package com.newscheck.newscheck.services.implementations;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.security.Key;
import java.util.Date;

/*
    Developed by Group 6:
        Ken Aliling
        Anicia Kaela Bonayao
        Carl Norbi Felonia
        Cedrick Miguel Kaneko
        Dino Alfred T. Timbol (Group Leader)
 */

//JWT token provider service. Contains business logic
//for jwt token provider operations

@Component
public class JwtTokenProvider {

    //References jwt secret key stored in the
    //application.properties file
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    //References jwt token expiration date stored in the
    //application.properties file
    @Value("${jwt.expiration}")
    private long EXPIRATION_TIME;

    //Generates JWT token using hmac
    public String generateToken(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    //Validates a JWT token
    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setAllowedClockSkewSeconds(60)
                    .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            System.out.println("Token expires at: " + claims.getExpiration());
            System.out.println("Current time: " + new Date());

            return claims.getExpiration().after(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("Invalid JWT token: " + e.getMessage());
            return false;
        }
    }

    //Retrieves email associated with a JWT token
    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    //Retrieves expiration date of a JWT token
    public Date getExpirationDateFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getExpiration();
    }
}

