package com.newscheck.newscheck.services;

import com.newscheck.newscheck.models.TokenBlackList;
import com.newscheck.newscheck.repositories.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class LogoutService implements ILogoutService {

    private final TokenRepository tokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void blacklistToken(String token) {
        if (token != null) {
            Date expiryDate = jwtTokenProvider.getExpirationDateFromToken(token);
            TokenBlackList blacklistedToken = new TokenBlackList(token, expiryDate.toInstant());
            tokenRepository.save(blacklistedToken);
        }
    }

    @Override
    public boolean isTokenBlacklisted(String token) {
        return tokenRepository.existsByToken(token);
    }

    @Override
    @Scheduled(fixedRate = 3600000)
    public void purgeExpiredTokens() {
        tokenRepository.deleteByExpiryDateBefore(Instant.now());
    }
}