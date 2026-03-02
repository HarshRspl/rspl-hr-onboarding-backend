package com.rspl.onboarding.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final HrUserService hrUserService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        // Authenticate
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(), request.getPassword()
                )
        );

        String username = request.getUsername().toLowerCase();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Get HR user meta
        Map<String, String> user = hrUserService.getUser(username);
        if (user == null) {
            return ResponseEntity.status(401).body(
                    Map.of("success", false, "message", "Invalid credentials")
            );
        }

        String appRole = user.get("role");      // HR_ADMIN / HR / HR_MANAGER
        String displayName = user.get("name");  // e.g., Sneha Kapoor

        // Add ROLE_ prefix for Spring authorities in token
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "ROLE_" + appRole);

        String token = jwtService.generateToken(claims, userDetails);

        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("username", username);
        data.put("name", displayName);
        data.put("role", appRole);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", data
        ));
    }

    // Used by login.html to verify token and auto-redirect
    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body(
                    Map.of("success", false, "message", "Not authenticated")
            );
        }

        String username = (String) authentication.getPrincipal();
        Map<String, String> user = hrUserService.getUser(username.toLowerCase());
        if (user == null) {
            return ResponseEntity.status(401).body(
                    Map.of("success", false, "message", "User not found")
            );
        }

        Map<String, Object> data = new HashMap<>();
        data.put("username", username);
        data.put("name", user.get("name"));
        data.put("role", user.get("role"));

        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", data
        ));
    }
}
