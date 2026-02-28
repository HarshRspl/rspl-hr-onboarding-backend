package com.rspl.onboarding.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    @Value("${jwt.secret:rspl_jwt_secret_key_minimum_32_characters_long_2026}")
    private String secret;

    private static final long HR_TOKEN_EXPIRY_MS = 8 * 60 * 60 * 1000L; // 8 hours

    private Key getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateHRToken(String username, String role) {
        return Jwts.builder()
            .setSubject(username)
            .addClaims(Map.of("role", role, "type", "HR_SESSION"))
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + HR_TOKEN_EXPIRY_MS))
            .signWith(getKey(), SignatureAlgorithm.HS512)
            .compact();
    }

    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return (String) getClaims(token).get("role");
    }

    public boolean isHRToken(String token) {
        return "HR_SESSION".equals(getClaims(token).get("type"));
    }

    public boolean isValid(String token) {
        try {
            Claims claims = getClaims(token);
            return claims.getExpiration().after(new Date()) && isHRToken(token);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(getKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }
}
