package com.newscheck.newscheck.services.implementations;

import com.newscheck.newscheck.models.*;
import com.newscheck.newscheck.models.enums.Role;
import com.newscheck.newscheck.models.responses.LoginResponse;
import com.newscheck.newscheck.models.responses.RegisterResponse;
import com.newscheck.newscheck.models.responses.ResetResponse;
import com.newscheck.newscheck.models.responses.SettingsResponse;
import com.newscheck.newscheck.repositories.PasswordTokenRepository;
import com.newscheck.newscheck.repositories.UserRepository;
import com.newscheck.newscheck.services.interfaces.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class authService implements IAuthService, UserDetailsService  {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailService emailService;
    private final ForgotService forgotService;
    private final PasswordTokenRepository passwordTokenRepository;


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

        PasswordResetToken tokenRecord = passwordTokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new RuntimeException("Invalid or expired token"));

        if (tokenRecord.getExpiry().isBefore(LocalDateTime.now())) {
            return new ResetResponse("Token expired");
        }

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        passwordTokenRepository.delete(tokenRecord);

        return new ResetResponse("Password has been reset successfully.");
    }

    @Override
    public ResetResponse forgotPassword(ForgotModel request) {

        UserModel user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + request.getEmail()));

        PasswordResetToken tokenRecord = passwordTokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new RuntimeException("Invalid or expired token"));

        if (tokenRecord.getExpiry().isBefore(LocalDateTime.now())) {
            return new ResetResponse("Token expired");
        }

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        passwordTokenRepository.delete(tokenRecord);

        return new ResetResponse("Password has been reset successfully.");
    }

    @Override
    public String requestReset(String email) {
        Optional<UserModel> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            UserModel user = optionalUser.get();
            String token = UUID.randomUUID().toString();
            LocalDateTime expiry = LocalDateTime.now().plusHours(1);

            PasswordResetToken resetToken = new PasswordResetToken();
            resetToken.setToken(token);
            resetToken.setUser(user);
            resetToken.setExpiry(expiry);
            passwordTokenRepository.save(resetToken);

            emailService.sendResetPasswordEmail(email, token);

            return "If your email exists, a reset link has been sent.";
        }

        return null;
    }

    @Override
    public String requestForgot(String email) {
        Optional<UserModel> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            UserModel user = optionalUser.get();
            String token = UUID.randomUUID().toString();
            LocalDateTime expiry = LocalDateTime.now().plusHours(1);

            PasswordResetToken resetToken = new PasswordResetToken();
            resetToken.setToken(token);
            resetToken.setUser(user);
            resetToken.setExpiry(expiry);
            passwordTokenRepository.save(resetToken);

            forgotService.sendResetPasswordEmail(email, token);

            return "If your email exists, a reset link has been sent.";
        }

        return null;
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

    @Override
    public boolean isEmailValid(String email) {
        return userRepository.existsByEmail(email);
    }
}
