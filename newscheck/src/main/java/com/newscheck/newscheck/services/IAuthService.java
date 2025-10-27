package com.newscheck.newscheck.services;

import com.newscheck.newscheck.models.*;
import com.newscheck.newscheck.models.responses.LoginResponse;
import com.newscheck.newscheck.models.responses.RegisterResponse;
import com.newscheck.newscheck.models.responses.ResetResponse;
import com.newscheck.newscheck.models.responses.SettingsResponse;

import java.util.Optional;

public interface IAuthService {

    RegisterResponse register(RegisterModel request);
    LoginResponse login(LoginModel request);
    ResetResponse resetPassword(ResetModel request);
    SettingsResponse updateUser(SettingsModel request);
    SettingsModel getUser(long id);
    Long getUserIdByEmail(String email);
    String requestReset(String email);
}

