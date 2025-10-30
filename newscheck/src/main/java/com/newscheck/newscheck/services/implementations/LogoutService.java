package com.newscheck.newscheck.services.implementations;

import com.newscheck.newscheck.models.TokenBlackList;
import com.newscheck.newscheck.repositories.TokenRepository;
import com.newscheck.newscheck.services.interfaces.ILogoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Date;

/*
    Developed by Group 6:
        Ken Aliling
        Anicia Kaela Bonayao
        Carl Norbi Felonia
        Cedrick Miguel Kaneko
        Dino Alfred T. Timbol (Group Leader)
 */

//Logout service. Contains business logic
//for log out operations

@Service
@RequiredArgsConstructor
public class LogoutService implements ILogoutService {

    private final TokenRepository tokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    //Blacklists a JWT token once a user logs out
    @Override
    public void blacklistToken(String token) {
        if (token != null) {
            Date expiryDate = jwtTokenProvider.getExpirationDateFromToken(token);
            TokenBlackList blacklistedToken = new TokenBlackList(token, expiryDate.toInstant());
            tokenRepository.save(blacklistedToken);
        }
    }

    //Checks whether a JWT token has already been blacklisted
    @Override
    public boolean isTokenBlacklisted(String token) {
        return tokenRepository.existsByToken(token);
    }

    //Automatically blacklists token that exists longer than 24 hours
    @Override
    @Scheduled(fixedRate = 3600000)
    public void purgeExpiredTokens() {
        tokenRepository.deleteByExpiryDateBefore(Instant.now());
    }
}