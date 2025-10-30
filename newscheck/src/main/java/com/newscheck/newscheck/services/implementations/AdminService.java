package com.newscheck.newscheck.services.implementations;

import com.newscheck.newscheck.models.UserModel;
import com.newscheck.newscheck.models.VerificationModel;
import com.newscheck.newscheck.models.enums.Role;
import com.newscheck.newscheck.models.enums.VerificationStatus;
import com.newscheck.newscheck.models.enums.VerdictType;
import com.newscheck.newscheck.models.responses.UserResponseDTO;
import com.newscheck.newscheck.models.responses.VerificationResponseDTO;
import com.newscheck.newscheck.repositories.AuditLogRepository;
import com.newscheck.newscheck.repositories.UserRepository;
import com.newscheck.newscheck.repositories.VerificationRepository;
import com.newscheck.newscheck.services.interfaces.IAdminService;
import com.newscheck.newscheck.services.interfaces.IVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
    Developed by Group 6:
        Ken Aliling
        Anicia Kaela Bonayao
        Carl Norbi Felonia
        Cedrick Miguel Kaneko
        Dino Alfred T. Timbol (Group Leader)
 */

//Admin service. Contains business logic
//for admin operations

@Service
@RequiredArgsConstructor
public class AdminService implements IAdminService {

    private final UserRepository userRepository;
    private final VerificationRepository verificationRepository;
    private final AuditLogRepository auditLogRepository;
    private final IVerificationService verificationService;

    //Retrieves all users paginated
    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDTO> getAllUsers(Pageable pageable, String email, String role) {
        Specification<UserModel> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (email != null && !email.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%"));
            }

            if (role != null && !role.isEmpty()) {
                try {
                    Role roleEnum = Role.valueOf(role.toUpperCase());
                    predicates.add(cb.equal(root.get("role"), roleEnum));
                } catch (IllegalArgumentException e) {
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<UserModel> users = userRepository.findAll(spec, pageable);
        return users.map(this::convertToUserResponseDTO);
    }

    //Retrieves a particular user
    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long userId) throws Exception {
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found with ID: " + userId));
        return convertToUserResponseDTO(user);
    }

    //Deletes a particular user
    @Override
    @Transactional
    public void deleteUser(Long userId) throws Exception {
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found with ID: " + userId));

        if (user.getRole() == Role.ADMIN) {
            throw new Exception("Cannot delete admin users");
        }

        userRepository.delete(user);
    }

    //Retrieves all verifications paginated
    @Override
    @Transactional(readOnly = true)
    public Page<VerificationResponseDTO> getAllVerifications(
            Pageable pageable, Long userId, String status, String verdictType) {

        Specification<VerificationModel> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (userId != null) {
                predicates.add(cb.equal(root.get("user").get("userId"), userId));
            }

            if (status != null && !status.isEmpty()) {
                try {
                    VerificationStatus statusEnum = VerificationStatus.valueOf(status.toUpperCase());
                    predicates.add(cb.equal(root.get("status"), statusEnum));
                } catch (IllegalArgumentException e) {

                }
            }

            if (verdictType != null && !verdictType.isEmpty()) {
                try {
                    VerdictType verdictEnum = VerdictType.valueOf(verdictType.toUpperCase());
                    predicates.add(cb.equal(root.get("verdict").get("verdictType"), verdictEnum));
                } catch (IllegalArgumentException e) {

                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<VerificationModel> verifications = verificationRepository.findAll(spec, pageable);
        return verifications.map(this::convertToVerificationResponseDTO);
    }

    //Retrieves a particular verification
    @Override
    @Transactional(readOnly = true)
    public VerificationResponseDTO getVerificationById(Long verificationId) throws Exception {
        return verificationService.getVerificationResult(verificationId);
    }

    //Deletes a particular verification
    @Override
    @Transactional
    public void deleteVerification(Long verificationId) throws Exception {
        VerificationModel verification = verificationRepository.findById(verificationId)
                .orElseThrow(() -> new Exception("Verification not found with ID: " + verificationId));

        verificationRepository.delete(verification);
    }

    //Retrieves statistics for admin dashboard
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getDashboardStatistics() {
        Map<String, Object> stats = new HashMap<>();

        long totalUsers = userRepository.count();
        long adminUsers = userRepository.countByRole(Role.ADMIN);
        long regularUsers = userRepository.countByRole(Role.USER);

        stats.put("totalUsers", totalUsers);
        stats.put("adminUsers", adminUsers);
        stats.put("regularUsers", regularUsers);

        long totalVerifications = verificationRepository.count();
        long verifiedCount = verificationRepository.countByStatus(VerificationStatus.VERIFIED);
        long pendingCount = verificationRepository.countByStatus(VerificationStatus.PENDING);
        long failedCount = verificationRepository.countByStatus(VerificationStatus.FAILED);

        stats.put("totalVerifications", totalVerifications);
        stats.put("verifiedCount", verifiedCount);
        stats.put("pendingCount", pendingCount);
        stats.put("failedCount", failedCount);

        Map<String, Long> verdictStats = new HashMap<>();
        for (VerdictType verdictType : VerdictType.values()) {
            long count = verificationRepository.countByVerdict_VerdictType(verdictType);
            verdictStats.put(verdictType.name(), count);
        }
        stats.put("verdictTypeStats", verdictStats);

        long totalAuditLogs = auditLogRepository.count();
        stats.put("totalAuditLogs", totalAuditLogs);

        LocalDateTime weekAgo = LocalDateTime.now().minusDays(7);
        long recentVerifications = verificationRepository.countBySubmittedAtAfter(weekAgo);
        long recentAuditLogs = auditLogRepository.countByTimestampAfter(weekAgo);

        stats.put("verificationsLast7Days", recentVerifications);
        stats.put("auditLogsLast7Days", recentAuditLogs);

        LocalDateTime monthAgo = LocalDateTime.now().minusDays(30);
        long newUsers = userRepository.countByCreatedAtAfter(monthAgo);
        stats.put("newUsersLast30Days", newUsers);

        return stats;
    }

    //Converts user information to response object that can be used by the
    //frontend
    private UserResponseDTO convertToUserResponseDTO(UserModel user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setUserId(user.getUserId());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setGender(user.getGender());
        dto.setCountry(user.getCountry());
        dto.setLanguage(user.getLanguage());
        dto.setRole(user.getRole());
        dto.setCreatedAt(user.getCreatedAt());

        long verificationCount = verificationRepository.countByUser_UserId(user.getUserId());
        dto.setTotalVerifications(verificationCount);

        long auditLogCount = auditLogRepository.countByUser_UserId(user.getUserId());
        dto.setTotalAuditLogs(auditLogCount);

        return dto;
    }

    //Converts verificatio information to response object that can be used by the
    //frontend
    private VerificationResponseDTO convertToVerificationResponseDTO(VerificationModel verification) {
        VerificationResponseDTO dto = new VerificationResponseDTO();
        dto.setVerificationId(verification.getVerificationId());
        dto.setStatus(verification.getStatus());
        dto.setSubmittedAt(verification.getSubmittedAt());

        if (verification.getContentText() != null && !verification.getContentText().isEmpty()) {
            String claim = verification.getContentText();
            dto.setClaim(claim.length() > 100 ? claim.substring(0, 100) + "..." : claim);
        }

        if (verification.getVerdict() != null) {
            dto.setVerdictType(verification.getVerdict().getVerdictType());
            dto.setReasoning(verification.getVerdict().getReasoning());
            dto.setCompletedAt(verification.getVerdict().getVerdictDate());
        }

        dto.setConfidenceScore(verification.getScore());

        return dto;
    }
}