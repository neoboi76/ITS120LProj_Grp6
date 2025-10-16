package com.newscheck.newscheck.controllers;

import com.newscheck.newscheck.models.*;
import com.newscheck.newscheck.services.authService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private authService auth;

    public UserController() {
        System.out.println("UserController loaded!");
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginModel logUser) throws BadRequestException {
       return ResponseEntity.ok(auth.login(logUser));
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterModel regUser) throws BadRequestException {
        System.out.println("Register");
        return ResponseEntity.ok(auth.register(regUser));
    }

}
