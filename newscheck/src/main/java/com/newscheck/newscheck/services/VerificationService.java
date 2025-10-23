package com.newscheck.newscheck.services;

import com.newscheck.newscheck.models.*;
import com.newscheck.newscheck.models.requests.EvidenceDTO;
import com.newscheck.newscheck.models.requests.VerificationRequestDTO;
import com.newscheck.newscheck.models.responses.VerificationResponseDTO;
import com.newscheck.newscheck.models.enums.VerificationStatus;
import com.newscheck.newscheck.models.Gemini.GeminiAnalysisResult;
import com.newscheck.newscheck.repositories.EvidenceRepository;
import com.newscheck.newscheck.repositories.UserRepository;
import com.newscheck.newscheck.repositories.VerdictRepository;
import com.newscheck.newscheck.repositories.VerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VerificationService implements IVerificationService {

    private final VerificationRepository verificationRepository;
    private final VerdictRepository verdictRepository;
    private final EvidenceRepository evidenceRepository;
    private final UserRepository userRepository;
    private final IGeminiService geminiService;
    private final IUrlContentExtractorService urlContentExtractorService;
    private final IImageTextExtractorService imageTextExtractorService;
    private final IGoogleSearchService googleSearchService;

    @Override
    @Transactional
    public VerificationResponseDTO submitVerification(VerificationRequestDTO request) throws Exception {
        // Validate request
        com.newscheck.newscheck.utils.ContentValidator.validateVerificationRequest(request);

        // Validate user
        UserModel user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new Exception("User not found"));

        // Create verification record
        VerificationModel verification = new VerificationModel();
        verification.setUser(user);
        verification.setContentType(request.getContentType());
        verification.setStatus(VerificationStatus.PENDING);
        verification.setSubmittedAt(LocalDateTime.now());

        // Extract content based on type
        String contentToAnalyze = "";

        try {
            switch (request.getContentType()) {
                case TEXT:
                    contentToAnalyze = request.getContentText();
                    verification.setContentText(contentToAnalyze);
                    break;

                case URL:
                    contentToAnalyze = urlContentExtractorService.extractContent(request.getContentUrl());
                    verification.setContentUrl(request.getContentUrl());
                    verification.setContentText(contentToAnalyze);
                    break;

                case IMAGE:
                    // Extract text from image using Gemini Vision
                    contentToAnalyze = imageTextExtractorService.extractTextFromImage(request.getImageBase64());
                    verification.setContentText(contentToAnalyze);
                    // Note: Image file is NOT saved - only extracted text is stored
                    // Base64 string is used temporarily and discarded
                    break;
            }

            // Save verification with PENDING status first
            verification = verificationRepository.save(verification);

            // Get the verification ID for potential rollback
            final Long verificationId = verification.getVerificationId();

            // Perform Google Search for real-time verification
            List<com.newscheck.newscheck.models.search.SearchResult> searchResults = new ArrayList<>();
            try {
                // Extract optimal search query from content
                String searchQuery = com.newscheck.newscheck.utils.SearchQueryExtractor.extractSearchQuery(contentToAnalyze);
                searchQuery = com.newscheck.newscheck.utils.SearchQueryExtractor.enhanceQuery(searchQuery);
                searchResults = googleSearchService.searchNews(searchQuery, 5);
            } catch (Exception searchEx) {
                // If search fails, continue with analysis without search results
                System.out.println("Search failed, proceeding without search results: " + searchEx.getMessage());
            }

            // Analyze content with Gemini (with or without search results)
            GeminiAnalysisResult analysisResult;
            if (!searchResults.isEmpty()) {
                analysisResult = geminiService.analyzeContentWithSearch(contentToAnalyze, searchResults);
            } else {
                analysisResult = geminiService.analyzeContent(contentToAnalyze);
            }

            // Create verdict
            VerdictModel verdict = new VerdictModel();
            verdict.setVerification(verification);
            verdict.setVerdictType(analysisResult.getVerdict());
            verdict.setReasoning(analysisResult.getReasoning());
            verdict.setVerdictDate(LocalDateTime.now());
            verdict.setCreatedAt(LocalDateTime.now());
            verdict = verdictRepository.save(verdict);

            // Create evidence records
            List<EvidenceModel> evidences = new ArrayList<>();
            if (analysisResult.getSources() != null) {
                for (GeminiAnalysisResult.SourceEvidence source : analysisResult.getSources()) {
                    EvidenceModel evidence = new EvidenceModel();
                    evidence.setVerdict(verdict);
                    evidence.setSourceName(source.getSourceName());
                    evidence.setSourceUrl(source.getSourceUrl());
                    evidence.setDescription(source.getDescription());
                    evidence.setRelevanceScore(source.getRelevanceScore());
                    evidences.add(evidence);
                }
                evidenceRepository.saveAll(evidences);
            }

            // Update verification status and score
            verification.setStatus(VerificationStatus.VERIFIED);
            verification.setScore(analysisResult.getConfidenceScore());
            verification.setVerdict(verdict);
            verification = verificationRepository.save(verification);

            // Build response
            return buildVerificationResponse(verification, verdict, evidences, analysisResult.getConfidenceScore());

        } catch (Exception e) {
            // Log the actual error
            System.err.println("Verification failed with error: " + e.getMessage());
            e.printStackTrace();

            // Try to update verification status to FAILED (in a separate transaction if needed)
            try {
                verification.setStatus(VerificationStatus.FAILED);
                verificationRepository.save(verification);
            } catch (Exception saveEx) {
                System.err.println("Could not save FAILED status: " + saveEx.getMessage());
            }

            // Re-throw with more context
            throw new Exception("Verification failed: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public VerificationResponseDTO getVerificationResult(Long verificationId) throws Exception {
        VerificationModel verification = verificationRepository.findById(verificationId)
                .orElseThrow(() -> new Exception("Verification not found"));

        VerdictModel verdict = verification.getVerdict();
        List<EvidenceModel> evidences = verdict != null ? verdict.getEvidences() : new ArrayList<>();

        return buildVerificationResponse(verification, verdict, evidences, verification.getScore());
    }

    @Override
    @Transactional(readOnly = true)
    public List<VerificationResponseDTO> getUserVerifications(Long userId) throws Exception {
        List<VerificationModel> verifications = verificationRepository.findByUser_UserIdOrderBySubmittedAtDesc(userId);

        return verifications.stream()
                .map(v -> {
                    VerdictModel verdict = v.getVerdict();
                    List<EvidenceModel> evidences = verdict != null ? verdict.getEvidences() : new ArrayList<>();
                    return buildVerificationResponse(v, verdict, evidences, v.getScore());
                })
                .collect(Collectors.toList());
    }

    private VerificationResponseDTO buildVerificationResponse(VerificationModel verification,
                                                              VerdictModel verdict,
                                                              List<EvidenceModel> evidences,
                                                              Double confidenceScore) {
        VerificationResponseDTO response = new VerificationResponseDTO();
        response.setVerificationId(verification.getVerificationId());
        response.setStatus(verification.getStatus());
        response.setSubmittedAt(verification.getSubmittedAt());

        response.setClaim(verification.getContentText());

        if (verdict != null) {
            response.setVerdictType(verdict.getVerdictType());
            response.setReasoning(verdict.getReasoning());
            response.setCompletedAt(verdict.getVerdictDate());
            response.setConfidenceScore(confidenceScore);

            List<EvidenceDTO> evidenceDTOs = evidences.stream()
                    .map(e -> new EvidenceDTO(
                            e.getEvidenceId(),
                            e.getSourceName(),
                            e.getSourceUrl(),
                            e.getDescription(),
                            e.getRelevanceScore()
                    ))
                    .collect(Collectors.toList());
            response.setEvidences(evidenceDTOs);
        }

        String message = switch (verification.getStatus()) {
            case VERIFIED -> "Verification completed successfully";
            case PENDING -> "Verification is in progress";
            case FAILED -> "Verification failed";
        };
        response.setMessage(message);

        return response;
    }
}