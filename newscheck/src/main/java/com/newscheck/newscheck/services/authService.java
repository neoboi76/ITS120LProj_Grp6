package com.newscheck.newscheck.services;

import com.newscheck.newscheck.models.*;
import com.newscheck.newscheck.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class authService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    //private AuditLogService auditLogService;

    public LoginResponse register(RegisterModel request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            try {
                throw new BadRequestException("Email already exists");
            } catch (BadRequestException e) {
                throw new RuntimeException(e);
            }
        }


        UserModel user = new UserModel();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        UserModel savedUser = userRepository.save(user);
        //auditLogService.log(savedUser, "REGISTER", "User registered");

        LoginModel logUser = new LoginModel(savedUser.getEmail(), savedUser.getPasswordHash());

        try {
            return login(logUser);
        } catch (BadRequestException e) {
            throw new RuntimeException(e);
        }
    }

    public LoginResponse login(LoginModel request) throws BadRequestException {
        UserModel user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            try {
                throw new BadRequestException("Invalid credentials");
            } catch (BadRequestException e) {
                throw new RuntimeException(e);
            }
        }

        String token = jwtTokenProvider.generateToken(user.getEmail());
        //auditLogService.log(user, "LOGIN", "User logged in");

        LoginResponse response = new LoginResponse(token, user.getEmail(), "User has been logged in");

        return response;
    }

/*
    public void logout(User user) {
        auditLogService.log(user, "LOGOUT", "User logged out");
    }
*/

    /*
    public void resetPassword(User user, ResetPasswordRequest request) {
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        auditLogService.log(user, "PASSWORD_RESET", "Password reset");
    }*/
}
