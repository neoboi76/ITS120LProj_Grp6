package com.newscheck.newscheck.controllers;

import com.newscheck.newscheck.models.*;
import com.newscheck.newscheck.models.responses.*;
import com.newscheck.newscheck.repositories.UserRepository;
import com.newscheck.newscheck.services.JwtTokenProvider;
import com.newscheck.newscheck.services.LogoutService;
import com.newscheck.newscheck.services.authService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
    public ResponseEntity<?> login(@RequestBody LoginModel request) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken
                            (request.getEmail(), request.getPassword())
            );

            LoginResponse response = authService.login(request);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);


        } catch(Exception e) {
            return ResponseEntity.badRequest().body("User does not exist");
        }


    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterModel request) {

        RegisterResponse response = authService.register(request);

        if (response == null) {
            return ResponseEntity.badRequest().body("Invalid signup");
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);

    }

    @PutMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetModel request) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getOldPassword())
            );

            ResetResponse response = authService.resetPassword(request);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Password reset failed");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        try {
            String token = getTokenFromRequest(request);
            logoutService.blacklistToken(token);
            return ResponseEntity.ok(new LogoutResponse("Logout successful. Token has been invalidated."));

        } catch(Exception e) {
            return ResponseEntity.badRequest().body("Logout unsuccessful");
        }
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}