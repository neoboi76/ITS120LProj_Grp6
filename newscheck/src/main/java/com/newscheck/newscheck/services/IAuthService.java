package com.newscheck.newscheck.services;

import com.newscheck.newscheck.models.LoginModel;
import com.newscheck.newscheck.models.RegisterModel;
import com.newscheck.newscheck.models.ResetModel;
import com.newscheck.newscheck.models.SettingsModel;
import com.newscheck.newscheck.models.responses.LoginResponse;
import com.newscheck.newscheck.models.responses.RegisterResponse;
import com.newscheck.newscheck.models.responses.ResetResponse;
import com.newscheck.newscheck.models.responses.SettingsResponse;

public interface IAuthService {

    RegisterResponse register(RegisterModel request);
    LoginResponse login(LoginModel request);
    ResetResponse resetPassword(ResetModel request);
    SettingsResponse updateUser(SettingsModel request);
    SettingsModel getUser(long id);
}

