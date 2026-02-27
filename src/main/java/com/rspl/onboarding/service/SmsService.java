package com.rspl.onboarding.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class SmsService {

    @Value("${fast2sms.api.key:NOT_SET}")
    private String apiKey;

    private static final String BASE_URL =
        "https://rspl-hr-onboarding-backend-production.up.railway.app";

    public void sendOnboardingLink(String mobile, String candidateName, String token) {
        try {
            String link = BASE_URL + "/onboarding.html?token=" + token;
            String message = "Dear " + candidateName +
                ", Welcome to RSPL! Complete your onboarding: " + link + " -RSPL HR";

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("authorization", apiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> body = new HashMap<>();
            body.put("route", "q");
            body.put("message", message);
            body.put("language", "english");
            body.put("flash", 0);
            body.put("numbers", mobile);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(
                "https://www.fast2sms.com/dev/bulkV2",
                request,
                String.class
            );
            System.out.println("✅ SMS sent to " + mobile + " | " + response.getBody());

        } catch (Exception e) {
            System.err.println("❌ SMS failed for " + mobile + ": " + e.getMessage());
        }
    }
}
