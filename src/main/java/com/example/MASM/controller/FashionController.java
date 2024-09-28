package com.example.MASM.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.Map;

@RestController
@RequestMapping("/api/fashion")
public class FashionController {

    @PostMapping("/generate")
    public ResponseEntity<?> generateFashion(@RequestParam("image") MultipartFile image, @RequestParam("prompt") String prompt) {
        if (image.isEmpty()) {
            return ResponseEntity.badRequest().body("No image received");
        }

        try {
            // Prepare headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            // Prepare body with image and prompt
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("image", image.getResource());
            body.add("prompt", prompt);

            // Make a request to the model server (Python API)
            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.exchange("http://localhost:5000/generate-fashion", HttpMethod.POST, requestEntity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                // Get the generated image as a Base64 string
                String generatedImageBase64 = response.getBody();
                System.out.println("image: "+ generatedImageBase64);
                // Verify the response is not null and contains the expected Base64 data
                if (generatedImageBase64 != null && !generatedImageBase64.isEmpty()) {
                    // Optionally decode and log for debugging
                    byte[] imageBytes = Base64.getDecoder().decode(generatedImageBase64);
                    System.out.println("Received generated image of size: " + imageBytes.length);

                    // Return the Base64 image as a JSON response
                    return ResponseEntity.ok(Map.of("image", generatedImageBase64));
                } else {
                    return ResponseEntity.status(500).body("Received empty or invalid image data from model server");
                }
            } else {
                return ResponseEntity.status(response.getStatusCode()).body("Error in generating image: " + response.getBody());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal server error: " + e.getMessage());
        }
    }
}
