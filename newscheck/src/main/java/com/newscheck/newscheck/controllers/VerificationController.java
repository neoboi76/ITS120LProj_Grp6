package com.newscheck.newscheck.controllers;

import com.newscheck.newscheck.models.enums.AuditAction;
import com.newscheck.newscheck.models.requests.VerificationRequestDTO;
import com.newscheck.newscheck.models.responses.VerificationResponseDTO;
import com.newscheck.newscheck.services.IAuditLogService;
import com.newscheck.newscheck.services.IVerificationService;
import com.newscheck.newscheck.services.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
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
    private final JwtTokenProvider jwtTokenProvider;
    private final IAuditLogService auditLogService;

    @PostMapping("/submit")
    public ResponseEntity<?> submitVerification(HttpServletRequest httpReq, @RequestBody VerificationRequestDTO request) {
        try {
            String token = getTokenFromRequest(httpReq);
            if (!this.jwtTokenProvider.validateToken(token)) {
                auditLogService.log(
                        AuditAction.VERIFICATION_FAILED,
                        request.getUserId(),
                        "Attempted verification submission with invalid token",
                        httpReq
                );
                return ResponseEntity.status(401).body("Unauthorized: Invalid token");
            }

            auditLogService.log(
                    AuditAction.VERIFICATION_SUBMITTED,
                    request.getUserId(),
                    String.format("Verification submitted - Type: %s", request.getContentType()),
                    httpReq
            );

            VerificationResponseDTO response = verificationService.submitVerification(request);

            auditLogService.log(
                    AuditAction.VERIFICATION_COMPLETED,
                    request.getUserId(),
                    "Verification successfully processed",
                    httpReq
            );

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            auditLogService.log(
                    AuditAction.VERIFICATION_FAILED,
                    request != null ? request.getUserId() : null,
                    "Verification submission failed: " + e.getMessage(),
                    httpReq
            );
            return ResponseEntity.badRequest().body("Validation error: " + e.getMessage());

        } catch (Exception e) {
            auditLogService.log(
                    AuditAction.VERIFICATION_FAILED,
                    request != null ? request.getUserId() : null,
                    "Verification submission error: " + e.getMessage(),
                    httpReq
            );
            return ResponseEntity.badRequest().body("Verification submission failed: " + e.getMessage());
        }
    }

    @GetMapping("/result/{verificationId}")
    public ResponseEntity<?> getVerificationResult(HttpServletRequest httpReq, @PathVariable Long verificationId) {
        try {
            String token = getTokenFromRequest(httpReq);
            if (token == null || !this.jwtTokenProvider.validateToken(token)) {
                auditLogService.log(
                        AuditAction.VERIFICATION_FAILED,
                        (Long) null,
                        "Unauthorized attempt to view verification result",
                        httpReq
                );
                return ResponseEntity.status(401).body("Unauthorized");
            }

            VerificationResponseDTO response = verificationService.getVerificationResult(verificationId);

            auditLogService.log(
                    AuditAction.VERIFICATION_VIEWED,
                    verificationId,
                    "Verification result viewed for ID: " + verificationId,
                    httpReq
            );

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            auditLogService.log(
                    AuditAction.VERIFICATION_FAILED,
                    (Long) null,
                    "Invalid verification ID: " + e.getMessage(),
                    httpReq
            );
            return ResponseEntity.badRequest().body("Invalid verification ID: " + e.getMessage());

        } catch (Exception e) {
            auditLogService.log(
                    AuditAction.VERIFICATION_FAILED,
                    (Long) null,
                    "Error retrieving verification result: " + e.getMessage(),
                    httpReq
            );
            return ResponseEntity.badRequest().body("Failed to retrieve verification result: " + e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserVerifications(HttpServletRequest httpReq, @PathVariable Long userId) {
        try {
            String token = getTokenFromRequest(httpReq);
            if (token == null || !this.jwtTokenProvider.validateToken(token)) {
                auditLogService.log(
                        AuditAction.VERIFICATION_FAILED,
                        userId,
                        "Unauthorized access attempt to user verifications",
                        httpReq
                );
                return ResponseEntity.status(401).body("Unauthorized");
            }

            List<VerificationResponseDTO> responses = verificationService.getUserVerifications(userId);

            auditLogService.log(
                    AuditAction.VERIFICATION_VIEWED,
                    userId,
                    "Viewed list of user verifications",
                    httpReq
            );

            return ResponseEntity.ok(responses);

        } catch (Exception e) {
            auditLogService.log(
                    AuditAction.VERIFICATION_FAILED,
                    userId,
                    "Error retrieving user verifications: " + e.getMessage(),
                    httpReq
            );
            return ResponseEntity.badRequest().body("Failed to retrieve user verifications: " + e.getMessage());
        }
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
