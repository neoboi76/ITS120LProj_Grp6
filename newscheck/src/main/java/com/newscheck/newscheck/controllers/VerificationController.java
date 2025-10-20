package com.newscheck.newscheck.controllers;

import com.newscheck.newscheck.models.requests.VerificationRequestDTO;
import com.newscheck.newscheck.models.responses.VerificationResponseDTO;
import com.newscheck.newscheck.services.IVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/verification")
@RequiredArgsConstructor
public class VerificationController {

    private final IVerificationService verificationService;

    @PostMapping("/submit")
    public ResponseEntity<?> submitVerification(@RequestBody VerificationRequestDTO request) {
        try {
            VerificationResponseDTO response = verificationService.submitVerification(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Verification submission failed: " + e.getMessage());
        }
    }

    @GetMapping("/result/{verificationId}")
    public ResponseEntity<?> getVerificationResult(@PathVariable Long verificationId) {
        try {
            VerificationResponseDTO response = verificationService.getVerificationResult(verificationId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to retrieve verification result: " + e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserVerifications(@PathVariable Long userId) {
        try {
            List<VerificationResponseDTO> responses = verificationService.getUserVerifications(userId);
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to retrieve user verifications: " + e.getMessage());
        }
    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<?> getVerificationHistory(@PathVariable Long userId) {
        try {
            List<VerificationResponseDTO> responses = verificationService.getUserVerifications(userId);
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to retrieve verification history: " + e.getMessage());
        }
    }
}