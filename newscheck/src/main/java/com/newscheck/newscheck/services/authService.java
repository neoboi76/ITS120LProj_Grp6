package com.newscheck.newscheck.services;

import com.newscheck.newscheck.models.*;
import com.newscheck.newscheck.models.enums.Role;
import com.newscheck.newscheck.models.responses.LoginResponse;
import com.newscheck.newscheck.models.responses.RegisterResponse;
import com.newscheck.newscheck.models.responses.ResetResponse;
import com.newscheck.newscheck.models.responses.SettingsResponse;
import com.newscheck.newscheck.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class authService implements IAuthService, UserDetailsService  {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;


    @Override
    public RegisterResponse register(RegisterModel request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalStateException("Email already exists");
        }

        UserModel user = new UserModel();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        if (request.getEmail().equals("admin@gmail.com")) {
            user.setRole(Role.ADMIN);
        } else {
            user.setRole(Role.USER);
        }

        UserModel savedUser = userRepository.save(user);

        return new RegisterResponse("Account Created!", savedUser.getEmail(), savedUser.getUserId());
    }

    @Override
    public LoginResponse login(LoginModel request) {

        UserModel user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found after successful authentication."));
        String token = jwtTokenProvider.generateToken(user.getEmail());

        return new LoginResponse(
                "Login Successful!",
                token,
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getGender(),
                user.getCountry(),
                user.getLanguage(),
                user.getRole().name(), // Include role in response
                user.getUserId()
        );
    }

    @Override
    public ResetResponse resetPassword(ResetModel request) {
        UserModel user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + request.getEmail()));

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return new ResetResponse("Password has been reset successfully.");
    }

    @Override
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
    public SettingsModel getUser(long id) {
        UserModel user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new SettingsModel(
                user.getFirstName(),
                user.getLastName(),
                user.getUserId(),
                user.getGender(),
                user.getCountry(),
                user.getLanguage()
        );
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

    @Override
    public Long getUserIdByEmail(String email) {
        return userRepository.getUserIdByEmail(email);
    }
}