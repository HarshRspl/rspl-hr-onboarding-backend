package com.rspl.onboarding.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        // Skip JWT processing for all public/static paths
        String path = request.getServletPath();
        if (isPublicPath(path)) {
            chain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                if (jwtService.isValid(token)) {
                    String username = jwtService.extractUsername(token);
                    String role     = jwtService.extractRole(token);

                    // Ensure ROLE_ prefix exists for Spring Security
                    String authority = role.startsWith("ROLE_") ? role : "ROLE_" + role;

                    UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            List.of(new SimpleGrantedAuthority(authority))
                        );
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (Exception ignored) {}
        }

        chain.doFilter(request, response);
    }

    private boolean isPublicPath(String path) {
        return path.endsWith(".html")
            || path.endsWith(".js")
            || path.endsWith(".css")
            || path.endsWith(".ico")
            || path.endsWith(".png")
            || path.endsWith(".jpg")
            || path.endsWith(".svg")
            || path.endsWith(".woff")
            || path.endsWith(".woff2")
            || path.equals("/error")
            || path.equals("/health")
            || path.startsWith("/api/auth/")
            || path.startsWith("/api/candidate/")
            || path.startsWith("/static/")
            || path.startsWith("/css/")
            || path.startsWith("/js/")
            || path.startsWith("/images/");
    }
}
