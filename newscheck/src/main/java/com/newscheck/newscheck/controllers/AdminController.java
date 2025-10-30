package com.newscheck.newscheck.controllers;

import com.newscheck.newscheck.models.responses.UserResponseDTO;
import com.newscheck.newscheck.models.responses.VerificationResponseDTO;
import com.newscheck.newscheck.services.interfaces.IAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/*
    Developed by Group 6:
        Ken Aliling
        Anicia Kaela Bonayao
        Carl Norbi Felonia
        Cedrick Miguel Kaneko
        Dino Alfred T. Timbol (Group Leader)
 */

//Admin controller containing endpoints and operations
//for handling admin requests

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')") // Class-level: Only admins can access
public class AdminController {

    private final IAdminService adminService;


    //Retrieves all users paginated
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "userId") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String role
    ) {
        try {
            Sort.Direction direction = sortDirection.equalsIgnoreCase("ASC")
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC;

            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

            Page<UserResponseDTO> users = adminService.getAllUsers(pageable, email, role);

            Map<String, Object> response = new HashMap<>();
            response.put("content", users.getContent());
            response.put("currentPage", users.getNumber());
            response.put("totalItems", users.getTotalElements());
            response.put("totalPages", users.getTotalPages());
            response.put("pageSize", users.getSize());
            response.put("hasNext", users.hasNext());
            response.put("hasPrevious", users.hasPrevious());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Failed to retrieve users: " + e.getMessage());
        }
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Long userId) {
        try {
            UserResponseDTO user = adminService.getUserById(userId);
            return ResponseEntity.ok(user);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Failed to retrieve user: " + e.getMessage());
        }
    }

    //Retrieves all verifications paginated
    @GetMapping("/verifications")
    public ResponseEntity<?> getAllVerifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "submittedAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String verdictType
    ) {
        try {
            Sort.Direction direction = sortDirection.equalsIgnoreCase("ASC")
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC;

            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

            Page<VerificationResponseDTO> verifications = adminService.getAllVerifications(
                    pageable, userId, status, verdictType);

            Map<String, Object> response = new HashMap<>();
            response.put("content", verifications.getContent());
            response.put("currentPage", verifications.getNumber());
            response.put("totalItems", verifications.getTotalElements());
            response.put("totalPages", verifications.getTotalPages());
            response.put("pageSize", verifications.getSize());
            response.put("hasNext", verifications.hasNext());
            response.put("hasPrevious", verifications.hasPrevious());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Failed to retrieve verifications: " + e.getMessage());
        }
    }

    //Retrieves a particular verification
    @GetMapping("/verifications/{verificationId}")
    public ResponseEntity<?> getVerificationById(@PathVariable Long verificationId) {
        try {
            VerificationResponseDTO verification = adminService.getVerificationById(verificationId);
            return ResponseEntity.ok(verification);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Failed to retrieve verification: " + e.getMessage());
        }
    }

    //Retrieves all summarized statistics (total users, verifications, audit logs, etc.)
    @GetMapping("/dashboard/stats")
    public ResponseEntity<?> getDashboardStats() {
        try {
            Map<String, Object> stats = adminService.getDashboardStatistics();
            return ResponseEntity.ok(stats);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Failed to retrieve dashboard statistics: " + e.getMessage());
        }
    }

    //Deletes a particular user
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        try {
            adminService.deleteUser(userId);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "User deleted successfully");
            response.put("userId", userId);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Failed to delete user: " + e.getMessage());
        }
    }
    //Deletes a particular verification
    @DeleteMapping("/verifications/{verificationId}")
    public ResponseEntity<?> deleteVerification(@PathVariable Long verificationId) {
        try {
            adminService.deleteVerification(verificationId);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Verification deleted successfully");
            response.put("verificationId", verificationId);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Failed to delete verification: " + e.getMessage());
        }
    }
}