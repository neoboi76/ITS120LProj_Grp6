package com.newscheck.newscheck.controllers;

import com.newscheck.newscheck.models.*;
import com.newscheck.newscheck.models.enums.AuditAction;
import com.newscheck.newscheck.models.requests.ResetDTO;
import com.newscheck.newscheck.models.responses.*;
import com.newscheck.newscheck.services.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final IAuthService authService;
    private final ILogoutService logoutService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final IAuditLogService auditLogService;


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

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
