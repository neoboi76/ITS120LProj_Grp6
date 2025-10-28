package com.newscheck.newscheck.services.interfaces;

import com.newscheck.newscheck.models.requests.VerificationRequestDTO;
import com.newscheck.newscheck.models.responses.VerificationResponseDTO;

import java.util.List;

public interface IVerificationService {

    VerificationResponseDTO submitVerification(VerificationRequestDTO request) throws Exception;
    VerificationResponseDTO getVerificationResult(Long verificationId) throws Exception;
    List<VerificationResponseDTO> getUserVerifications(Long userId) throws Exception;

}