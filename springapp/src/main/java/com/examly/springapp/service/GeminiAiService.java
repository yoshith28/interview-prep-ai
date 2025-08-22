package com.examly.springapp.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.*;

@Service
@SuppressWarnings("unchecked")
public class GeminiAiService {
    // @Value("${gemini.api.key}")
    private String apiKey="AIzaSyCxyuAJQEYpE8dLdpz_lpUsUj2pAQmZfD8";

    private static final String GEMINI_API_URL = "YOUR_URL";

    public String getAiResponse(String prompt) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    Map<String, Object> requestBody = new HashMap<>();
    List<Map<String, Object>> contents = new ArrayList<>();
    Map<String, Object> content = new HashMap<>();
    List<Map<String, String>> parts = new ArrayList<>();
    Map<String, String> part = new HashMap<>();
    part.put("text", prompt);
    parts.add(part);
    content.put("parts", parts);
    contents.add(content);
    requestBody.put("contents", contents);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        // Use .fromUriString instead of deprecated .fromHttpUrl
        String url = UriComponentsBuilder.fromUriString(GEMINI_API_URL)
                .queryParam("key", apiKey)
                .toUriString();
        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(url, HttpMethod.POST, entity, (Class<Map<String, Object>>)(Class<?>)Map.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> body = response.getBody();
                if (body.containsKey("candidates")) {
                    List<Map<String, Object>> candidates = (List<Map<String, Object>>) body.get("candidates");
                    if (candidates != null && !candidates.isEmpty()) {
                        Map<String, Object> first = candidates.get(0);
                        Map<String, Object> contentObj = (Map<String, Object>) first.get("content");
                        List<Map<String, Object>> partsList = (List<Map<String, Object>>) contentObj.get("parts");
                        if (partsList != null && !partsList.isEmpty()) {
                            Map<String, Object> partMap = partsList.get(0);
                            return (String) partMap.get("text");
                        }
                    }
                }
            }
        } catch (Exception e) {
            return "AI Error: " + e.getMessage();
        }
        return "AI Error: No response";
    }
}
