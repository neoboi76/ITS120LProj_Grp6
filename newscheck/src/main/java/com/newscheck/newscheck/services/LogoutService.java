package com.newscheck.newscheck.services;

import com.newscheck.newscheck.models.TokenBlackList;
import com.newscheck.newscheck.repositories.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Date;

@Service
public class LogoutService {

    private final TokenRepository tokenRepository;
    private final JwtTokenProvider jwtTokenProvider; // To get the token's expiry date

    @Autowired
    public LogoutService(TokenRepository tokenRepository, JwtTokenProvider jwtTokenProvider) {
        this.tokenRepository = tokenRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // Adds a token to the blocklist table
    public void blacklistToken(String token) {
        if (token != null) {
            Date expiryDate = jwtTokenProvider.getExpirationDateFromToken(token);
            TokenBlackList blacklistedToken = new TokenBlackList(token, expiryDate.toInstant());
            tokenRepository.save(blacklistedToken);
        }
    }

    // Checks if a token is in the blocklist
    public boolean isTokenBlacklisted(String token) {
        return tokenRepository.existsByToken(token);
    }

    // This scheduled task runs every hour to clean up the database
    @Scheduled(fixedRate = 3600000)
    public void purgeExpiredTokens() {
        tokenRepository.deleteByExpiryDateBefore(Instant.now());
    }
}