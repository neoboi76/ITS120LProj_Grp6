package com.newscheck.newscheck.services.interfaces;

import com.newscheck.newscheck.models.responses.UserResponseDTO;
import com.newscheck.newscheck.models.responses.VerificationResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

/*
    Developed by Group 6:
        Ken Aliling
        Anicia Kaela Bonayao
        Carl Norbi Felonia
        Cedrick Miguel Kaneko
        Dino Alfred T. Timbol (Group Leader)
 */

//Interface for the admin service. Promotes loose coupling
//and dependency injection

public interface IAdminService {

    Page<UserResponseDTO> getAllUsers(Pageable pageable, String email, String role);

    UserResponseDTO getUserById(Long userId) throws Exception;

    void deleteUser(Long userId) throws Exception;

    Page<VerificationResponseDTO> getAllVerifications(Pageable pageable, Long userId, String status, String verdictType);

    VerificationResponseDTO getVerificationById(Long verificationId) throws Exception;

    void deleteVerification(Long verificationId) throws Exception;

    Map<String, Object> getDashboardStatistics();

}