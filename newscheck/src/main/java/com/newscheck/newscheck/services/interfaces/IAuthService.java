package com.newscheck.newscheck.services.interfaces;

import com.newscheck.newscheck.models.*;
import com.newscheck.newscheck.models.responses.LoginResponse;
import com.newscheck.newscheck.models.responses.RegisterResponse;
import com.newscheck.newscheck.models.responses.ResetResponse;
import com.newscheck.newscheck.models.responses.SettingsResponse;

/*
    Developed by Group 6:
        Ken Aliling
        Anicia Kaela Bonayao
        Carl Norbi Felonia
        Cedrick Miguel Kaneko
        Dino Alfred T. Timbol (Group Leader)
 */

//Interface for the auth service. Promotes loose coupling
//and dependency injection

public interface IAuthService {

    RegisterResponse register(RegisterModel request);
    LoginResponse login(LoginModel request);
    ResetResponse resetPassword(ResetModel request);
    SettingsResponse updateUser(SettingsModel request);
    SettingsModel getUser(long id);
    Long getUserIdByEmail(String email);
    String requestReset(String email);
    boolean isEmailValid(String email);
    ResetResponse forgotPassword(ForgotModel request);
    String requestForgot(String email);
}

