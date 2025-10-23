package com.newscheck.newscheck.controllers;

import com.newscheck.newscheck.models.enums.VerdictType;
import com.newscheck.newscheck.models.enums.VerificationStatus;
import com.newscheck.newscheck.models.requests.EvidenceDTO;
import com.newscheck.newscheck.models.requests.VerificationRequestDTO;
import com.newscheck.newscheck.models.responses.VerificationResponseDTO;
import com.newscheck.newscheck.services.IVerificationService;
import com.newscheck.newscheck.services.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/verification")
@RequiredArgsConstructor
public class VerificationController {

    private final IVerificationService verificationService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/submit")
    public ResponseEntity<?> submitVerification(HttpServletRequest httpReq, @RequestBody VerificationRequestDTO request) {
        try {

            System.out.println(this.jwtTokenProvider.validateToken(getTokenFromRequest(httpReq)));

            if (this.jwtTokenProvider.validateToken(getTokenFromRequest(httpReq))) {
                VerificationResponseDTO response = verificationService.submitVerification(request);
                return ResponseEntity.ok(response);
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Verification submission failed: " + e.getMessage());
        }
        return null;
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

    /*
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getMockVerifications(@PathVariable Long userId) {
        List<VerificationResponseDTO> mockVerifications = new ArrayList<>();

        // --- Mock 1: VERIFIED example ---
        VerificationResponseDTO verified = new VerificationResponseDTO();
        verified.setVerificationId(1L);
        verified.setStatus(VerificationStatus.VERIFIED);
        verified.setSubmittedAt(LocalDateTime.now().minusDays(2));
        verified.setVerdictType(VerdictType.TRUE);
        verified.setReasoning("Multiple credible news sources confirm the event.");
        verified.setCompletedAt(LocalDateTime.now().minusDays(1));
        verified.setConfidenceScore(0.93);
        verified.setMessage("Verification completed successfully");

        List<EvidenceDTO> verifiedEvidences = List.of(
                new EvidenceDTO(1L, "BBC News", "https://bbc.com/news/article", "Official report confirming the claim", 0.95),
                new EvidenceDTO(2L, "Reuters", "https://reuters.com/article", "Independent verification of the event", 0.89)
        );
        verified.setEvidences(verifiedEvidences);
        mockVerifications.add(verified);

        // --- Mock 2: FAILED example ---
        VerificationResponseDTO failed = new VerificationResponseDTO();
        failed.setVerificationId(2L);
        failed.setStatus(VerificationStatus.FAILED);
        failed.setSubmittedAt(LocalDateTime.now().minusDays(3));
        failed.setVerdictType(VerdictType.UNDEFINED);
        failed.setReasoning("Gemini service unavailable. Could not complete verification.");
        failed.setCompletedAt(LocalDateTime.now().minusDays(2));
        failed.setConfidenceScore(0.0);
        failed.setMessage("Verification failed");
        failed.setEvidences(new ArrayList<>());
        mockVerifications.add(failed);

        // --- Mock 3: PENDING example ---
        VerificationResponseDTO pending = new VerificationResponseDTO();
        pending.setVerificationId(3L);
        pending.setStatus(VerificationStatus.PENDING);
        failed.setVerdictType(VerdictType.FALSE);
        pending.setSubmittedAt(LocalDateTime.now().minusHours(5));
        pending.setVerdictType(null);
        pending.setReasoning(null);
        pending.setCompletedAt(null);
        pending.setConfidenceScore(null);
        pending.setMessage("Verification is in progress");
        pending.setEvidences(new ArrayList<>());
        mockVerifications.add(pending);

        return ResponseEntity.ok(mockVerifications);
    } */

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }


}