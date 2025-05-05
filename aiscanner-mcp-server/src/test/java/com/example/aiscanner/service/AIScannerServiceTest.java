package com.example.aiscanner.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@SpringBootTest
@TestPropertySource("classpath:test-application.properties")

class AIScannerServiceTest {

    @Mock
    private RestTemplate mockRestTemplate;

    @Autowired
    private AIScannerService aiScannerService;

    @Autowired
    private Environment environment;

    @BeforeEach
    void setUp() {
        // Create mock if not already created
        if (mockRestTemplate == null) {
            mockRestTemplate = mock(RestTemplate.class);
        }
        // Inject the mock RestTemplate into the service
        ReflectionTestUtils.setField(aiScannerService, "restTemplate", mockRestTemplate);
    }

    @Test
    void testRealSaplingEndpoint() {
        // Create a real service instance with actual RestTemplate
        RestTemplate realRestTemplate = new RestTemplate();

        String testApiKey = environment.getProperty("sapling.ai.api.key");
        AIScannerService realService = new AIScannerService(realRestTemplate, testApiKey);

        // Test with obviously AI-generated content
        String aiGeneratedContent =
                "The quantum fluctuations in the early universe led to the formation of " +
                        "galaxies through gravitational instabilities. These primordial density " +
                        "variations were amplified by inflation, creating the cosmic web structure " +
                        "we observe today. The interplay between dark matter and baryonic matter " +
                        "facilitated this complex evolution of cosmic structures.";

        // Call the real service - the method name is detect in the service
        Map<String, Object> result = realService.scan(aiGeneratedContent);

        // Print all response data for debugging
        System.out.println("Full API Response:");
        result.forEach((key, value) -> System.out.println(key + ": " + value));

        // Basic assertions
        assertThat(result).isNotNull();

        // Score might be 0.0 if there's an error, but the response should contain it
        assertThat(result).containsKey("score");

        // Print the score for verification
        Object scoreObj = result.get("score");
        if (scoreObj instanceof Number) {
            double score = ((Number)scoreObj).doubleValue();
            System.out.println("Real Sapling API Score for AI text: " + score);
        }
    }

    @Test
    // @EnabledIfEnvironmentVariable(named = "SAPLING_AI_API_KEY", matches = "[A-Za-z0-9]+")
    void testRealSaplingEndpointWithHumanText() {
        // Create a real service instance with actual RestTemplate
        RestTemplate realRestTemplate = new RestTemplate();

        String testApiKey = environment.getProperty("sapling.ai.api.key");
        AIScannerService realService = new AIScannerService(realRestTemplate, testApiKey);

        // Test with obviously human-written content
        String humanContent = "I went to the store yesterday. It was raining hard. " +
                "My umbrella broke on the way back and I got soaked. " +
                "Not a great day overall.";

        // Call the real service
        Map<String, Object> result = realService.scan(humanContent);

        // Print all response data for debugging
        System.out.println("Full API Response:");
        result.forEach((key, value) -> System.out.println(key + ": " + value));

        // Basic assertions
        assertThat(result).isNotNull();
        assertThat(result).containsKey("score");

        // Print the score for verification
        Object scoreObj = result.get("score");
        if (scoreObj instanceof Number) {
            double score = ((Number)scoreObj).doubleValue();
            System.out.println("Real Sapling API Score for human text: " + score);
        }
    }
}