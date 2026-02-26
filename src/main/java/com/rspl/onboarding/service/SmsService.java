package com.rspl.onboarding.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class SmsService {

    @Value("${fast2sms.api.key}")
    private String apiKey;

    public void sendOnboardingLink(String mobile, String candidateName, String token) {
        try {
            String onboardingLink = "https://rspl-hr-onboarding-backend-production.up.railway.app/onboarding.html?token=" + token;

            String message = "Dear " + candidateName + ", Welcome to RSPL! "
                    + "Please complete your onboarding form: "
                    + onboardingLink
                    + " - RSPL HR Team";

            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.set("authorization", apiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> body = new HashMap<>();
            body.put("route", "q");  // transactional route
            body.put("message", message);
            body.put("language", "english");
            body.put("flash", 0);
            body.put("numbers", mobile);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

            restTemplate.postForObject(
                "https://www.fast2sms.com/dev/bulkV2",
                request,
                String.class
            );

            System.out.println("✅ SMS sent to: " + mobile);

        } catch (Exception e) {
            System.err.println("❌ SMS failed: " + e.getMessage());
        }
    }
}
