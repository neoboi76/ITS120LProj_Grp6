package com.newscheck.newscheck.controllers;

import com.newscheck.newscheck.models.*;
import com.newscheck.newscheck.models.enums.AuditAction;
import com.newscheck.newscheck.models.requests.ForgotDTO;
import com.newscheck.newscheck.models.requests.ResetDTO;
import com.newscheck.newscheck.models.responses.*;
import com.newscheck.newscheck.services.implementations.JwtTokenProvider;
import com.newscheck.newscheck.services.interfaces.IAuditLogService;
import com.newscheck.newscheck.services.interfaces.IAuthService;
import com.newscheck.newscheck.services.interfaces.ILogoutService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

/*
    Developed by Group 6:
        Ken Aliling
        Anicia Kaela Bonayao
        Carl Norbi Felonia
        Cedrick Miguel Kaneko
        Dino Alfred T. Timbol (Group Leader)
 */

//User controller containing endpoints and operations
//for handling user requests

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final IAuthService authService;
    private final ILogoutService logoutService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final IAuditLogService auditLogService;


    //Handles login authentication and logging
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginModel request, HttpServletRequest httpRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            LoginResponse response = authService.login(request);

            auditLogService.log(
                    AuditAction.USER_LOGIN,
                    response.getId(),
                    "User logged in successfully",
                    httpRequest
            );

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);

        } catch (Exception e) {
            auditLogService.log(
                    AuditAction.LOGIN_FAILED,
                    (UserModel) null,
                    "Failed login attempt for email: " + request.getEmail(),
                    httpRequest
            );
            return ResponseEntity.badRequest().body("User does not exist");
        }
    }

    //Handles register or sign-up operations and logging
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterModel request, HttpServletRequest httpRequest) {
        try {
            RegisterResponse response = authService.register(request);
            if (response == null) {
                throw new RuntimeException("Invalid signup");
            }

            auditLogService.log(
                    AuditAction.USER_REGISTER,
                    response.getId(),
                    "New user registered: " + response.getEmail(),
                    httpRequest
            );

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);

        } catch (Exception e) {
            auditLogService.log(
                    AuditAction.SYSTEM_ERROR,
                    (UserModel) null,
                    "Failed registration attempt for email: " + request.getEmail(),
                    httpRequest
            );
            return ResponseEntity.badRequest().body("User does not exist");
        }
    }

    //Handles reset-password operations and logging
    @PutMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetModel request, HttpServletRequest httpRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getOldPassword())
            );


            ResetResponse response = authService.resetPassword(request);

            Long userId = authService.getUserIdByEmail(request.getEmail());
            auditLogService.log(
                    AuditAction.PASSWORD_RESET,
                    userId,
                    "User reset password",
                    httpRequest
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            auditLogService.log(
                    AuditAction.SYSTEM_ERROR,
                    (UserModel) null,
                    "Failed password reset attempt for email: " + request.getEmail(),
                    httpRequest
            );
            return ResponseEntity.badRequest().body("User does not exist");
        }
    }

    //Handles forgot-password operations and logging
    @PutMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotModel request, HttpServletRequest httpRequest) {
        try {

            if(authService.isEmailValid(request.getEmail())) {
                ResetResponse response = authService.forgotPassword(request);

                Long userId = authService.getUserIdByEmail(request.getEmail());
                auditLogService.log(
                        AuditAction.PASSWORD_RESET,
                        userId,
                        "User reset password",
                        httpRequest
                );

                return ResponseEntity.ok(response);
            }

        } catch (Exception e) {
            auditLogService.log(
                    AuditAction.SYSTEM_ERROR,
                    (UserModel) null,
                    "Failed password reset attempt for email: " + request.getEmail(),
                    httpRequest
            );
            return ResponseEntity.badRequest().body("User does not exist");
        }

        return null;
    }


    //Handles requests reset password (from inside settings page)
    @PostMapping("/request-reset")
    public ResponseEntity<?> requestReset(@RequestBody ResetDTO body, HttpServletRequest request) {
        try {
            String email = body.getEmail();

            String msg = authService.requestReset(email);

            return ResponseEntity.ok(msg);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Password reset was unsuccessful");
        }

    }

    //Handles requests forgot password (from the login page)
    @PostMapping("/request-forgot")
    public ResponseEntity<?> requestForgot(@RequestBody ForgotDTO body, HttpServletRequest request) {
        try {

            System.out.println(body.getEmail());

            String email = body.getEmail();

            String msg = authService.requestForgot(email);

            return ResponseEntity.ok(msg);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Password reset was unsuccessful");
        }

    }

    //Handles logout operations, jwt token blacklist, and logging
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        try {
            String token = getTokenFromRequest(request);
            String email = jwtTokenProvider.getEmailFromToken(token);
            Long userId = authService.getUserIdByEmail(email);

            logoutService.blacklistToken(token);

            auditLogService.log(
                    AuditAction.USER_LOGOUT,
                    userId,
                    "User logged out",
                    request
            );

            return ResponseEntity.ok(new LogoutResponse("Logout successful. Token has been invalidated."));

        } catch (Exception e) {
            auditLogService.log(
                    AuditAction.SYSTEM_ERROR,
                    (UserModel) null,
                    "Failed logout attempt",
                    request
            );
            return ResponseEntity.badRequest().body("User does not exist");
        }
    }

    //Handles updating user information and logging
    @PutMapping("/update-user")
    public ResponseEntity<?> updateUser(HttpServletRequest request, @RequestBody SettingsModel usrReq) {
        try {
            if (jwtTokenProvider.validateToken(getTokenFromRequest(request))) {
                SettingsResponse response = authService.updateUser(usrReq);

                auditLogService.log(
                        AuditAction.USER_PROFILE_UPDATED,
                        usrReq.getId(),
                        "User profile updated",
                        request
                );

                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(response);
            }
        } catch (Exception e) {
            auditLogService.log(
                    AuditAction.SYSTEM_ERROR,
                    (UserModel) null,
                    "Failed profile update for user ID: " + usrReq.getId(),
                    request
            );
            return ResponseEntity.badRequest().body("User does not exist");
        }

        return ResponseEntity.badRequest().body("User does not exist");
    }

    //Retrieves a particular user and logs it
    @GetMapping("/get-user/{id}")
    public ResponseEntity<?> getUser(HttpServletRequest request, @PathVariable long id) {
        try {
            if (jwtTokenProvider.validateToken(getTokenFromRequest(request))) {
                SettingsModel response = authService.getUser(id);

                auditLogService.log(
                        AuditAction.USER_PROFILE_VIEWED,
                        id,
                        "User profile viewed",
                        request
                );

                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            auditLogService.log(
                    AuditAction.SYSTEM_ERROR,
                    (UserModel) null,
                    "Failed to fetch user profile with ID: " + id,
                    request
            );
            return ResponseEntity.badRequest().body("User does not exist");
        }

        return ResponseEntity.badRequest().body("User does not exist");
    }

    //Gets JWT token from request body 
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
