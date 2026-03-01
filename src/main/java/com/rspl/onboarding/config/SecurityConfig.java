package com.rspl.onboarding.config;

import com.rspl.onboarding.auth.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // ✅ Same domain (static served by Spring Boot) + local dev
        config.setAllowedOrigins(List.of(
            "https://rspl-hr-onboarding-backend-production.up.railway.app",
            "http://localhost:8080",
            "http://localhost:5173",
            "http://localhost:3000"
        ));

        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept", "X-Requested-With"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(false); // ✅ JWT in header = false
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(Customizer.withDefaults())
            .sessionManagement(sess -> sess
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // ✅ Allow preflight requests
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // ✅ Public static pages
                .requestMatchers(
                    "/",
                    "/index.html",
                    "/onboarding.html",
                    "/hr-dashboard.html",
                    "/login.html",
                    "/health",
                    "/favicon.ico",
                    "/error"
                ).permitAll()

                // ✅ Static assets
                .requestMatchers(
                    "/static/**",
                    "/css/**",
                    "/js/**",
                    "/images/**",
                    "/*.js",
                    "/*.css",
                    "/*.png",
                    "/*.ico"
                ).permitAll()

                // ✅ Public API endpoints
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/candidate/**").permitAll()

                // ✅ HR APIs require JWT
                .requestMatchers("/api/hr/**").authenticated()

                // Everything else needs auth
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
