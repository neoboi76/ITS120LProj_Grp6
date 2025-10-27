package com.newscheck.newscheck.repositories;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.newscheck.newscheck.models.TokenBlackList;

@Repository
public interface TokenRepository extends JpaRepository<TokenBlackList, Long> {

    boolean existsByToken(String token);

    void deleteByExpiryDateBefore(Instant now);

    Optional<TokenBlackList> findByToken(String token);
}