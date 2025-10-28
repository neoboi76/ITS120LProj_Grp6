package com.newscheck.newscheck.services.interfaces;

public interface ILogoutService {
    void blacklistToken(String token);
    boolean isTokenBlacklisted(String token);
    void purgeExpiredTokens();
}
