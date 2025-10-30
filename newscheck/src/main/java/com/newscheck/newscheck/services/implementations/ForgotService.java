package com.newscheck.newscheck.services.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/*
    Developed by Group 6:
        Ken Aliling
        Anicia Kaela Bonayao
        Carl Norbi Felonia
        Cedrick Miguel Kaneko
        Dino Alfred T. Timbol (Group Leader)
 */

//forgot password service Contains business logic
//for forgot password email request  operations

@Service
@RequiredArgsConstructor
public class ForgotService {//Service class for requesting reset of password from the login page

    private final JavaMailSender mailSender;

    public void sendResetPasswordEmail(String toEmail, String token) {
        try { //forgot password reset link
            String resetLink = "http://localhost:4200/forgot-password?token=" + token;

            //Builds email message
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
