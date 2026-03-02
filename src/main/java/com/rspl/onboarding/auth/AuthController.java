package com.rspl.onboarding.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService    userDetailsService;
    private final JwtService            jwtService;
    private final HrUserService         hrUserService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {

        if (request.getUsername() == null || request.getPassword() == null) {
            return ResponseEntity.badRequest().body(
                    Map.of("success", false, "message", "Username and password are required")
            );
        }

        String username = request.getUsername().toLowerCase().trim();

        // ✅ FIX 1 — wrap in try-catch so bad credentials return 401, not 500
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, request.getPassword())
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(
                    Map.of("success", false, "message", "Invalid username or password")
            );
        } catch (DisabledException e) {
            return ResponseEntity.status(403).body(
                    Map.of("success", false, "message", "Account is disabled")
            );
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    Map.of("success", false, "message", "Authentication error: " + e.getMessage())
            );
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // ✅ FIX 2 — null-safe check on HrUserService
        Map<String, String> user = hrUserService.getUser(username);
        if (user == null) {
            return ResponseEntity.status(401).body(
                    Map.of("success", false, "message", "HR user not found")
            );
        }

        String appRole    = user.get("role");
        String displayName = user.get("name");

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "ROLE_" + appRole);

        String token = jwtService.generateToken(claims, userDetails);

        Map<String, Object> data = new HashMap<>();
        data.put("token",    token);
        data.put("username", username);
        data.put("name",     displayName);
        data.put("role",     appRole);

        return ResponseEntity.ok(Map.of("success", true, "data", data));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body(
                    Map.of("success", false, "message", "Not authenticated")
            );
        }

        String username = authentication.getName().toLowerCase();
        Map<String, String> user = hrUserService.getUser(username);
        if (user == null) {
            return ResponseEntity.status(401).body(
                    Map.of("success", false, "message", "User not found")
            );
        }

        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", Map.of(
                        "username", username,
                        "name",     user.get("name"),
                        "role",     user.get("role")
                )
        ));
    }
}
