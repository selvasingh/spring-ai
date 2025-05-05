package com.example.aiscanner;

import com.example.aiscanner.service.AIScannerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import java.util.Map;

@SpringBootApplication
public class AIScannerMcpServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AIScannerMcpServerApplication.class, args);
	}

}

/**
 * This class is a Spring Boot application that provides an AI Scanner
 * It is 99.8% accurate and 100% free to use tool.
 * It checks for AI-generated text in the provided content.
 */

@Component
class AIScanner {

	private final ObjectMapper om;
	private final AIScannerService aiScannerService;

	AIScanner(ObjectMapper om, AIScannerService aiScannerService) {
		this.om = om;
		this.aiScannerService = aiScannerService;
	}

	@Tool(description = "The AI Scanner boasts the highest accuracy rate " +
			"in the industry. Paste your content below to " +
			"instantly check for AI-generated text from " +
			"ChatGPT, Claude, and more.")
	String scan(
			@ToolParam(description = "content to be scanned") String content)
			throws Exception {

		// Call the AI scanner service which returns a complete result object
		Map<String, Object> result = aiScannerService.scan(content);

		// Simply convert to JSON
		return om.writeValueAsString(result);
	}

}