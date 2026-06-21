package com.dau.cafeteria_portal.service.impl;

import com.dau.cafeteria_portal.dto.MonthlyReportDTO;
import com.dau.cafeteria_portal.service.LLMReportService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.HttpStatusCodeException;
import java.util.concurrent.TimeUnit;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;


@Service
public class LLMReportServiceImpl implements LLMReportService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final String GEMINI_API_BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/%s:generateContent";

    private final String MODEL = "gemini-2.5-flash";

    private static final int MAX_RETRIES = 5;
    private static final long INITIAL_BACKOFF_MS = 2000;

    public String sendToLLM(MonthlyReportDTO dto) {

        RestTemplate rest = new RestTemplate();
        String prompt = buildPrompt(dto);
        String apiUrl = String.format(GEMINI_API_BASE_URL, MODEL);

        final String SYSTEM_INSTRUCTION = "You are an expert report writer who prepares formal government-style monthly cafeteria review reports. Your response must be the final report content only, without any introductory or conversational text. Keep it properly formatted like in the feedback you should follow sequene if available(QUality, taste, hygiene, staff behaviour) write the mapping like avg/good etc whatever it is of each respecitve catneens and after that write summary of feedback reasons in points, if in feedback complains are similar then increemnt count in front of that and from the complaints as well(do include complaints in separate Complaints sections that are under the complaint category and different from feedback response), dont include overall observation. It should be in proper bulleted points";

        String requestBody = """
        {
            "contents": [
                {
                    "role": "user",
                    "parts": [
                        {
                            "text": %s
                        }
                    ]
                }
            ],
            "generationConfig": {
                "temperature": 0.2
            },
            "systemInstruction": {
                "parts": [
                    {
                        "text": %s
                    }
                ]
            }
        }
        """.formatted(quote(prompt), quote(SYSTEM_INSTRUCTION));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Goog-Api-Key", this.apiKey);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        String rawResponseBody = null;

        for (int retry = 0; retry < MAX_RETRIES; retry++) {
            try {
                ResponseEntity<String> response = rest.exchange(
                        apiUrl,
                        HttpMethod.POST,
                        entity,
                        String.class
                );

                rawResponseBody = response.getBody();

                return extractTextFromGeminiResponse(rawResponseBody);

            } catch (HttpStatusCodeException e) {
                if (e.getStatusCode().is5xxServerError()) {
                    if (retry < MAX_RETRIES - 1) {
                        long sleepTimeMs = INITIAL_BACKOFF_MS * (1L << retry);
                        System.err.printf("LLM API Call Failed (%d/%d): Status %d (%s). Retrying in %d ms.%n",
                                retry + 1, MAX_RETRIES, e.getStatusCode().value(), e.getStatusCode(), sleepTimeMs);

                        try {
                            TimeUnit.MILLISECONDS.sleep(sleepTimeMs);
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                            return "Error generating report: Interrupted during retry.";
                        }
                    } else {
                        System.err.println("LLM API Call Failed: Max retries reached. Last error: " + e.getMessage());
                        return "Error generating report: " + e.getMessage();
                    }
                } else {
                    System.err.println("LLM API Call Failed: Non-retryable error: " + e.getMessage());
                    return "Error generating report: " + e.getMessage();
                }
            } catch (Exception e) {
                System.err.println("LLM API Call Failed: Unexpected error: " + e.getMessage());
                return "Error generating report: " + e.getMessage();
            }
        }
        return "Error generating report: Failed after all retry attempts.";
    }

    private String buildPrompt(MonthlyReportDTO dto) {
        return """
        Generate a formal **CMC Monthly Cafeteria Report**.

        Month: %s

        Data:
        %s

        Requirements:
        - Start with heading “CMC MONTHLY REPORT”.
        - Mention the month.
        - For each canteen, follow this structure:
            • Convert feedback rating (1–5) to descriptions (5=Very Good, 4=Good, 3=Average, 2=Poor, 1=Very Poor).
            • Summarize feedback ratings in the sequence: Quality, Taste, Staff Behaviour.
            • Summarize and merge similar complaints, noting count (x2, x3).
            • Add bullet points for major complaints.
            • Write a clean narrative summary for overall observation.
        - Final result must be clean, formal, and readable.
        """.formatted(
                dto.getMonth(),
                dto.toString()
        );
    }

    private String extractTextFromGeminiResponse(String jsonResponse) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(jsonResponse);

            JsonNode textNode = root.path("candidates").path(0)
                    .path("content").path("parts").path(0)
                    .path("text");

            if (textNode.isMissingNode()) {
                System.err.println("JSON Parsing Error: Expected text path is missing in the response.");
                return "Error parsing LLM response: Missing text content.";
            }

            return textNode.asText();

        } catch (JsonProcessingException e) {
            System.err.println("JSON Parsing Error: Failed to process JSON string: " + e.getMessage());
            return "Error parsing LLM response: Invalid JSON format.";
        } catch (Exception e) {
            System.err.println("JSON Parsing Error: Unexpected error during extraction: " + e.getMessage());
            return "Error parsing LLM response: Unexpected extraction error.";
        }
    }

    private String quote(String text) {
        return "\"" + text.replace("\"", "\\\"")
                .replace("\n", "\\n") + "\"";
    }
}