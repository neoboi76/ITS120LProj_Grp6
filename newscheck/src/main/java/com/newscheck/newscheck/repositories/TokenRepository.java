package com.newscheck.newscheck.repositories;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.newscheck.newscheck.models.TokenBlackList;

/*
    Developed by Group 6:
        Ken Aliling
        Anicia Kaela Bonayao
        Carl Norbi Felonia
        Cedrick Miguel Kaneko
        Dino Alfred T. Timbol (Group Leader)
 */

//token black list repository class, extending JpaRepository, containing
//db methods related to the token black list table

@Repository
public interface TokenRepository extends JpaRepository<TokenBlackList, Long> {

    boolean existsByToken(String token);

    void deleteByExpiryDateBefore(Instant now);

    Optional<TokenBlackList> findByToken(String token);
}