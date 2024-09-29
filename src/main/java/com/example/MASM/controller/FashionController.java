package com.example.MASM.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.Map;

@RestController
@RequestMapping()
public class FashionController {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @PostMapping("/generate")
    public ResponseEntity<byte[]> generateFashion(@RequestParam("image") MultipartFile image, @RequestParam("prompt") String prompt) {
        if (image.isEmpty()) {
            System.out.println("Image not received");
            return ResponseEntity.badRequest().body(null); // Return null if the image is not received
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", image.getResource());
        body.add("prompt", prompt);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:5000/generate-fashion", HttpMethod.POST, requestEntity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            String responseBody = response.getBody();

            // Log the response to understand its structure
            System.out.println("Response from backend: " + responseBody);

            try {
                // Parse the JSON response
                Map<String, Object> jsonResponse = objectMapper.readValue(responseBody, Map.class);
                String generatedImageBase64 = (String) jsonResponse.get("generated_image"); // Extract the base64 image data

                // Log the length of the base64 string to confirm it's received
                System.out.println("Received generated image with base64 length: " + generatedImageBase64.length());

                // Decode the base64 string to bytes
                byte[] imageBytes = Base64.getDecoder().decode(generatedImageBase64);
                System.out.println("Decoded image size: " + imageBytes.length);

                // Return the image bytes in the response
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_PNG) // Set content type to PNG
                        .body(imageBytes); // Send the image bytes in the response

            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body(null); // Return null if there's an error
            }
        } else {
            return ResponseEntity.status(response.getStatusCode()).body(null); // Return null on error
        }
    }


}
