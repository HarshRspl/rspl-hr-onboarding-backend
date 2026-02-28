package com.rspl.onboarding.api;

import com.rspl.onboarding.auth.HrUserService;
import com.rspl.onboarding.auth.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private HrUserService hrUserService;

    // POST /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        if (username == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Username and password are required"
            ));
        }

        Map<String, String> user = hrUserService.authenticate(username.trim(), password);
        if (user == null) {
            return ResponseEntity.status(401).body(Map.of(
                "success", false,
                "message", "Invalid username or password"
            ));
        }

        String token = jwtService.generateHRToken(username.trim(), user.get("role"));

        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Login successful",
            "data", Map.of(
                "token",       token,
                "username",    username.trim(),
                "name",        user.get("name"),
                "role",        user.get("role"),
                "expiresIn",   "8 hours"
            )
        ));
    }

    // GET /api/auth/me  — validate token & get current user
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> me(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "No token"));
        }
        String token = authHeader.substring(7);
        if (!jwtService.isValid(token)) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "Token invalid or expired"));
        }
        String username = jwtService.extractUsername(token);
        Map<String, String> user = hrUserService.getUser(username);
        return ResponseEntity.ok(Map.of(
            "success", true,
            "data", Map.of(
                "username", username,
                "name",     user != null ? user.get("name") : username,
                "role",     jwtService.extractRole(token)
            )
        ));
    }

    // POST /api/auth/logout  — client just deletes token, but we acknowledge
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout() {
        return ResponseEntity.ok(Map.of("success", true, "message", "Logged out successfully"));
    }
}
