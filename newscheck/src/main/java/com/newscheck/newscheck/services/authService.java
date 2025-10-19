package com.newscheck.newscheck.services;

import com.newscheck.newscheck.models.*;
import com.newscheck.newscheck.models.enums.Role;
import com.newscheck.newscheck.models.responses.LoginResponse;
import com.newscheck.newscheck.models.responses.RegisterResponse;
import com.newscheck.newscheck.models.responses.ResetResponse;
import com.newscheck.newscheck.models.responses.SettingsResponse;
import com.newscheck.newscheck.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class authService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public authService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public RegisterResponse register(RegisterModel request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalStateException("Email already exists");
        }

        UserModel user = new UserModel();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        UserModel savedUser = userRepository.save(user);


        return new RegisterResponse("Account Created!", savedUser.getEmail(), savedUser.getUserId());
    }

    public LoginResponse login(LoginModel request) {

        UserModel user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found after successful authentication."));
        String token = jwtTokenProvider.generateToken(user.getEmail());

        return new LoginResponse("Login Successful!", token, user.getEmail(), user.getFirstName(),
                user.getLastName(), user.getGender(), user.getCountry(), user.getLanguage(), user.getUserId());
    }

    public ResetResponse resetPassword(ResetModel request) {
        UserModel user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + request.getEmail()));

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return new ResetResponse("Password has been reset successfully.");
    }

    public SettingsResponse updateUser(SettingsModel request) {


        UserModel user = userRepository.findById(request.getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setGender(request.getGender());
        user.setCountry(request.getCountry());
        user.setLanguage(request.getLanguage());

        userRepository.save(user);

        return new SettingsResponse("User information succesfully updated");

    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPasswordHash(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );
    }
}