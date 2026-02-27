package com.rspl.onboarding.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class SmsService {

    @Value("${fast2sms.api.key:NOT_SET}")
    private String apiKey;

    /**
     * ✅ IMPORTANT:
     * Keep this as your app's public base URL (Railway domain).
     * Example:
     * https://rspl-hr-onboarding-backend-production.up.railway.app
     *
     * You can override via env:
     * APP_BASE_URL=https://your-app-domain
     */
    @Value("${app.base.url:https://rspl-hr-onboarding-backend-production.up.railway.app}")
    private String baseUrl;

    /**
     * ✅ This method is REQUIRED because your OnboardingService calls sendSms(mobile, message)
     * It sends the SMS using Fast2SMS.
     */
    public void sendSms(String mobile, String message) {
        try {
            if ("NOT_SET".equals(apiKey)) {
                System.out.println("⚠️ fast2sms.api.key is NOT_SET. SMS not sent. Message would be: " + message);
                return;
            }

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

    /**
     * ✅ Your existing method (kept)
     * Generates onboarding link and sends SMS.
     *
     * If you later REMOVE token, see the token-free version below.
     */
    public void sendOnboardingLink(String mobile, String candidateName, String token) {
        String link = baseUrl + "/onboarding.html?token=" + token;
        String message = "Dear " + candidateName +
                ", Welcome to RSPL! Complete your onboarding: " + link + " -RSPL HR";

        sendSms(mobile, message); // ✅ reuse common sender
    }

    /**
     * ✅ TOKEN-FREE version (use this only if you remove token from onboarding completely)
     * HR will send plain link (public form).
     */
    public void sendOnboardingLinkWithoutToken(String mobile, String candidateName) {
        String link = baseUrl + "/onboarding.html";
        String message = "Dear " + candidateName +
                ", Welcome to RSPL! Complete your onboarding: " + link + " -RSPL HR";

        sendSms(mobile, message);
    }
}
