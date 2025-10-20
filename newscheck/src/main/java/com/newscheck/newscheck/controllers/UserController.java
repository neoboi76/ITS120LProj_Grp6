package com.newscheck.newscheck.controllers;

import com.newscheck.newscheck.models.*;
import com.newscheck.newscheck.models.responses.*;
import com.newscheck.newscheck.services.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")

@RestController
@RequiredArgsConstructor
public class UserController {

    private final IAuthService authService;
    private final ILogoutService logoutService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

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

    @PutMapping("/update-user")
    public ResponseEntity<?> updateUser(HttpServletRequest request, @RequestBody SettingsModel usrReq) {

        try {

            if (this.jwtTokenProvider.validateToken(getTokenFromRequest(request))) {

                SettingsResponse response = authService.updateUser(usrReq);

                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(response);
            }


        } catch(Exception e) {
            return ResponseEntity.badRequest().body("User does not exist");
        }

        return null;
    }

    @GetMapping("/get-user/{id}")
    public ResponseEntity<?> getUser(HttpServletRequest request, @PathVariable long id) {

        try {

            if (this.jwtTokenProvider.validateToken(getTokenFromRequest(request))) {

                SettingsModel response = authService.getUser(id);

                return ResponseEntity.ok(response);
            }


        } catch(Exception e) {
            return ResponseEntity.badRequest().body("User does not exist");
        }

        return null;
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }


}