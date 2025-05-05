package com.example.aiscanner.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
public class AIScannerService {

    private static final Logger logger = LoggerFactory.getLogger(AIScannerService.class);

    // @Value("${sapling.ai.api.key}")
    // private String apiKey;
    private final String apiKey;

    private final RestTemplate restTemplate;
    private final String SAPLING_AI_DETECTOR_URL = "https://api.sapling.ai/api/v1/aidetect";

    /**
     * Constructor for AIScannerService
     *
     * @param restTemplate the RestTemplate to use for making API calls
     * @param apiKey       the API key for authentication
     */
    public AIScannerService(RestTemplate restTemplate, @Value("${sapling.ai.api.key}") String apiKey) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
    }

    @PostConstruct
    public void init() {
        if (apiKey == null || apiKey.isEmpty()) {
            logger.warn("Sapling AI API key not set. Service will return error responses unless key is provided.");
        }

        logger.debug("@PostConstruct App PHASE == Resolved API key: {}", apiKey);
    }

    /**
     * Invokes the Sapling AI detector API to check if content is AI-generated
     *
     * @param content The text to check
     * @return Map containing the AI detection results and metadata
     */
    public Map<String, Object> scan(String content) {
        Map<String, Object> result = new HashMap<>();
        result.put("input-draft-length", content.length());
        String truncatedContent = content.length() > 240
                ? content.substring(0, 240) + "..."
                : content;
        result.put("input-draft", truncatedContent);
        result.put("service-name", "AI Scanner");
        result.put("service-version", "1.0 (2025-05-01)");

        logger.debug("Inside scan == Resolved API key: {}", apiKey);

        if (apiKey == null || apiKey.isEmpty()) {
            result.put("detection-status", "error");
            result.put("error", "API key not configured");
            result.put("status", "failed");
            result.put("score", 0.0);
            logger.error("AI Scanner Error: Missing API key configuration");
            return result;
        }

        logger.info("\n");

        if (apiKey.length() > 4) {
            String maskedKey = apiKey.substring(0, 2) + "..." +
                    apiKey.substring(apiKey.length() - 2);
            logger.debug("Using API key starting with: {} - and key length: {}", maskedKey, apiKey.length());
        }

        logger.info("AI Scanner Request: {}\n", result);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("key", apiKey);
        requestBody.put("text", content);

        logger.debug("Request body contains key field: {}, key length: {}, text field length: {}",
                requestBody.containsKey("key"),
                ((String) requestBody.get("key")).length(),
                content.length());

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    SAPLING_AI_DETECTOR_URL,
                    HttpMethod.POST,
                    request,
                    Map.class);

            if (response == null || response.getBody() == null) {
                result.put("detection-status", "error");
                result.put("status", "failed");
                result.put("error", "Empty response from Sapling AI detector");
                logger.error("AI Scanner Error: Received empty response body from Sapling");
                return result;
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> apiResponse = response.getBody();

            logger.debug("Raw Sapling API response: {}", apiResponse);

            result.put("detection-status", "success");
            result.put("score", apiResponse.getOrDefault("score", 0.0));
            result.put("score-hint", "A score from 0 to 1 be returned, with 0 indicating the maximum confidence that the text is human-written, and 1 indicating the maximum confidence that the text is AI-generated");
            result.put("sapling-results", apiResponse);
            result.putAll(apiResponse);

            logger.info("AI Scanner Response: {}\n", result);
            return result;

        } catch (Exception e) {
            result.put("detection-status", "error");
            result.put("error", e.getMessage());
            result.put("status", "failed");
            logger.error("AI Scanner Error: {}\n", result);
            return result;
        }
    }

}
