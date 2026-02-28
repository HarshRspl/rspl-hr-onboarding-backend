package com.rspl.onboarding.auth;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.HashMap;

@Service
public class HrUserService {

    // username → { password, role, displayName }
    // Change passwords before going live!
    private static final Map<String, Map<String, String>> USERS = new HashMap<>();

    static {
        USERS.put("hradmin",  Map.of("password", "Admin@2026",  "role", "HR_ADMIN",   "name", "HR Admin"));
        USERS.put("sneha",    Map.of("password", "Sneha@2026",  "role", "HR",         "name", "Sneha Kapoor"));
        USERS.put("arjun",    Map.of("password", "Arjun@2026",  "role", "HR",         "name", "Arjun Mehta"));
        USERS.put("pooja",    Map.of("password", "Pooja@2026",  "role", "HR",         "name", "Pooja Nair"));
        USERS.put("kunal",    Map.of("password", "Kunal@2026",  "role", "HR",         "name", "Kunal Bose"));
        USERS.put("divya",    Map.of("password", "Divya@2026",  "role", "HR_MANAGER", "name", "Divya Rawat"));
    }

    public Map<String, String> authenticate(String username, String password) {
        Map<String, String> user = USERS.get(username.toLowerCase());
        if (user != null && user.get("password").equals(password)) {
            return user;
        }
        return null;
    }

    public Map<String, String> getUser(String username) {
        return USERS.get(username.toLowerCase());
    }
}
