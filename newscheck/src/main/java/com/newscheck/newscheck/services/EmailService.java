package com.newscheck.newscheck.services;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendResetPasswordEmail(String toEmail, String token) {
        try {
            String resetLink = "http://localhost:4200/reset-password?token=" + token;

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Reset Your Password");
            message.setText("Click the link below to reset your password:\n" + resetLink +
                    "\nThis link will expire in 1 hour.");
            message.setFrom("your.email@gmail.com"); // must match spring.mail.username
            System.out.println("Sending email to " + toEmail); // debug log
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
