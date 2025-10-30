package com.newscheck.newscheck.services.interfaces;

import com.newscheck.newscheck.models.requests.VerificationRequestDTO;
import com.newscheck.newscheck.models.responses.VerificationResponseDTO;

import java.util.List;

/*
    Developed by Group 6:
        Ken Aliling
        Anicia Kaela Bonayao
        Carl Norbi Felonia
        Cedrick Miguel Kaneko
        Dino Alfred T. Timbol (Group Leader)
 */

//Interface for the verification service. Promotes loose coupling
//and dependency injection

public interface IVerificationService {

    VerificationResponseDTO submitVerification(VerificationRequestDTO request) throws Exception;
    VerificationResponseDTO getVerificationResult(Long verificationId) throws Exception;
    List<VerificationResponseDTO> getUserVerifications(Long userId) throws Exception;

}