package com.newscheck.newscheck.controllers;

import com.newscheck.newscheck.models.*;
import com.newscheck.newscheck.models.responses.*;
import com.newscheck.newscheck.repositories.UserRepository;
import com.newscheck.newscheck.services.JwtTokenProvider;
import com.newscheck.newscheck.services.LogoutService;
import com.newscheck.newscheck.services.authService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final authService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final LogoutService logoutService;

    @Autowired
    public UserController(authService authService, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserRepository userRepository, LogoutService logoutService) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.logoutService = logoutService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginModel request) {
        // The controller triggers the authentication. If it fails, an exception is thrown automatically.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // This code only runs if authentication is successful
        UserModel user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found after successful authentication."));
        String token = jwtTokenProvider.generateToken(user.getEmail());
        return ResponseEntity.ok(new LoginResponse("Login Successful!", token, user.getEmail(), user.getUserId()));
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterModel request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PutMapping("/reset-password")
    public ResponseEntity<ResetResponse> resetPassword(@RequestBody ResetModel request) {
        // First, verify the user's old password is correct
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getOldPassword())
        );

        // If successful, call the service to perform the update
        ResetResponse response = authService.resetPassword(request);

        // Also log out the user by blacklisting any existing tokens
        // (This is an optional but good security practice)
        // You would need to implement a way to get all active tokens for a user if you want this feature.
        // For now, we will just reset the password.

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        logoutService.blacklistToken(token);
        return ResponseEntity.ok(new LogoutResponse("Logout successful. Token has been invalidated."));
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}