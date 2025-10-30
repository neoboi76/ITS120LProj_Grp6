package com.newscheck.newscheck.services.implementations;

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
import com.newscheck.newscheck.services.interfaces.*;
import com.newscheck.newscheck.utils.SearchQueryExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/*
    Developed by Group 6:
        Ken Aliling
        Anicia Kaela Bonayao
        Carl Norbi Felonia
        Cedrick Miguel Kaneko
        Dino Alfred T. Timbol (Group Leader)
 */

//Verification service. Contains business logic
//for verification operations

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

    //Submits verification
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
                case TEXT: //text cases. Set as it is
                    contentToAnalyze = request.getContentText();
                    verification.setContentText(contentToAnalyze);
                    break;

                case URL://If url, utilize the urlContentExtractorService
                    contentToAnalyze = urlContentExtractorService.extractContent(request.getContentUrl());
                    verification.setContentUrl(request.getContentUrl());
                    verification.setContentText(contentToAnalyze);
                    break;

                case IMAGE://If image, utilize the imageTextExtratorService
                    System.out.println("Processing IMAGE verification");
                    System.out.println("Base64 length: " + request.getImageBase64().length());

                    contentToAnalyze = imageTextExtractorService.extractTextFromImage(
                            request.getImageBase64()
                    );


                    System.out.println("Extracted text length: " + contentToAnalyze.length());
                    System.out.println("Extracted text preview: " +
                            contentToAnalyze.substring(0, Math.min(200, contentToAnalyze.length())));

                    verification.setContentText(contentToAnalyze);
                    verification.setImageBase64(request.getImageBase64());
                    break;
            }

            verification = verificationRepository.save(verification);

            final Long verificationId = verification.getVerificationId();

            List<com.newscheck.newscheck.models.search.SearchResult> searchResults = new ArrayList<>();
            boolean searchSucceeded = false;
            try {
                String searchQuery = SearchQueryExtractor.extractSearchQuery(contentToAnalyze);
                searchQuery = SearchQueryExtractor.enhanceQuery(searchQuery);

                //Search results to be retrieved is 5. (Though, max is 10).
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
            if (searchSucceeded && !searchResults.isEmpty()) {//Analyze with search results
                System.out.println("Analyzing with search results");
                analysisResult = geminiService.analyzeContentWithSearch(contentToAnalyze, searchResults);
            } else {
                System.out.println("Analyzing without search results (fallback mode)");
                analysisResult = geminiService.analyzeContent(contentToAnalyze);//Analyzing without search results
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

            //Builds verification response
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

    //Returns the verification result when analysis is done
    @Override
    @Transactional(readOnly = true)
    public VerificationResponseDTO getVerificationResult(Long verificationId) throws Exception {
        VerificationModel verification = verificationRepository.findById(verificationId)
                .orElseThrow(() -> new Exception("Verification not found"));

        VerdictModel verdict = verification.getVerdict();
        List<EvidenceModel> evidences = verdict != null ? verdict.getEvidences() : new ArrayList<>();

        return buildVerificationResponse(verification, verdict, evidences, verification.getScore());
    }

    //Returns verifications associated with a particular user
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

    //Builds verification response
    private VerificationResponseDTO buildVerificationResponse(VerificationModel verification,
                                                              VerdictModel verdict,
                                                              List<EvidenceModel> evidences,
                                                              Double confidenceScore) throws Exception {
        VerificationResponseDTO response = new VerificationResponseDTO();
        response.setVerificationId(verification.getVerificationId());
        response.setStatus(verification.getStatus());
        response.setSubmittedAt(verification.getSubmittedAt());

        //If text, set contentText as claim
        if (verification.getContentType() == ContentType.TEXT) {
            response.setClaim(verification.getContentText());
        }
        //If url, set contentUrl as claim
        if (verification.getContentType() == ContentType.URL) {
            response.setClaim(verification.getContentUrl());
        }

        //Otherwise, for image, just leave it blank

        //response.setContentType(verification.getContentType());
        //response.setClaim(verification.getContentText());
        //response.setContentUrl(verification.getContentUrl());

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