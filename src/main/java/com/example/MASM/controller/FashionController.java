package com.example.MASM.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/fashion")
public class FashionController {

    private static final String HUGGING_FACE_API_URL = "https://api-inference.huggingface.co/models/your_model_name_here";
    private static final String HUGGING_FACE_API_KEY = "YOUR_HUGGING_FACE_API_KEY";

    @PostMapping("/generate")
    public ResponseEntity<String> generateFashionImage(@RequestBody Map<String, String> request) {
        String userPrompt = request.get("prompt");

        if (userPrompt == null || userPrompt.isEmpty()) {
            return ResponseEntity.badRequest().body("Prompt cannot be empty.");
        }

        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + HUGGING_FACE_API_KEY);

            Map<String, String> body = new HashMap<>();
            body.put("inputs", userPrompt);

            HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    HUGGING_FACE_API_URL,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            return ResponseEntity.ok(response.getBody());

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error generating fashion image: " + e.getMessage());
        }
    }
}
