package com.newscheck.newscheck.services;

import com.newscheck.newscheck.models.*;
import com.newscheck.newscheck.models.enums.ContentType;
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

        com.newscheck.newscheck.utils.ContentValidator.validateVerificationRequest(request);

        UserModel user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new Exception("User not found"));

        VerificationModel verification = new VerificationModel();
        verification.setUser(user);
        verification.setContentType(request.getContentType());
        verification.setStatus(VerificationStatus.PENDING);
        verification.setSubmittedAt(LocalDateTime.now());

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
                    contentToAnalyze = imageTextExtractorService.extractTextFromImage(request.getImageBase64());
                    verification.setContentText(contentToAnalyze);
                    break;
            }

            verification = verificationRepository.save(verification);

            final Long verificationId = verification.getVerificationId();

            List<com.newscheck.newscheck.models.search.SearchResult> searchResults = new ArrayList<>();
            boolean searchSucceeded = false;
            try {
                String searchQuery = com.newscheck.newscheck.utils.SearchQueryExtractor.extractSearchQuery(contentToAnalyze);
                searchQuery = com.newscheck.newscheck.utils.SearchQueryExtractor.enhanceQuery(searchQuery);

                System.out.println("Performing search for: " + searchQuery);
                searchResults = googleSearchService.searchNews(searchQuery, 5);

                if (!searchResults.isEmpty()) {
                    searchSucceeded = true;
                    System.out.println("Search succeeded with " + searchResults.size() + " results");
                } else {
                    System.out.println("Search returned no results");
                }
            } catch (Exception searchEx) {

                System.err.println("Search failed: " + searchEx.getMessage());
                searchEx.printStackTrace();
            }

            GeminiAnalysisResult analysisResult;
            if (searchSucceeded && !searchResults.isEmpty()) {
                System.out.println("Analyzing with search results");
                analysisResult = geminiService.analyzeContentWithSearch(contentToAnalyze, searchResults);
            } else {
                System.out.println("Analyzing without search results (fallback mode)");
                analysisResult = geminiService.analyzeContent(contentToAnalyze);
            }

            VerdictModel verdict = new VerdictModel();
            verdict.setVerification(verification);
            verdict.setVerdictType(analysisResult.getVerdict());
            verdict.setReasoning(analysisResult.getReasoning());
            verdict.setVerdictDate(LocalDateTime.now());
            verdict.setCreatedAt(LocalDateTime.now());
            verdict = verdictRepository.save(verdict);

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

            verification.setStatus(VerificationStatus.VERIFIED);
            verification.setScore(analysisResult.getConfidenceScore());
            verification.setVerdict(verdict);
            verification = verificationRepository.save(verification);

            return buildVerificationResponse(verification, verdict, evidences, analysisResult.getConfidenceScore());

        } catch (Exception e) {
            System.err.println("Verification failed with error: " + e.getMessage());
            e.printStackTrace();

            try {
                verification.setStatus(VerificationStatus.FAILED);
                verificationRepository.save(verification);
            } catch (Exception saveEx) {
                System.err.println("Could not save FAILED status: " + saveEx.getMessage());
            }

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
                    try {
                        return buildVerificationResponse(v, verdict, evidences, v.getScore());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    private VerificationResponseDTO buildVerificationResponse(VerificationModel verification,
                                                              VerdictModel verdict,
                                                              List<EvidenceModel> evidences,
                                                              Double confidenceScore) throws Exception {
        VerificationResponseDTO response = new VerificationResponseDTO();
        response.setVerificationId(verification.getVerificationId());
        response.setStatus(verification.getStatus());
        response.setSubmittedAt(verification.getSubmittedAt());

        if (verification.getContentType() == ContentType.TEXT) {
            response.setClaim(verification.getContentText());
        }
        else if (verification.getContentType() == ContentType.URL) {
            response.setClaim((urlContentExtractorService.extractTitle(verification.getContentText())));
        }

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