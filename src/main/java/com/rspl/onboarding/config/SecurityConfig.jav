package com.rspl.onboarding.config;

import com.rspl.onboarding.auth.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // ── PUBLIC ─────────────────────────────────────────────
                .requestMatchers("/api/auth/**").permitAll()          // login/logout
                .requestMatchers("/api/candidate/**").permitAll()     // candidate portal
                .requestMatchers("/onboarding.html").permitAll()      // candidate form page
                .requestMatchers("/login.html").permitAll()           // HR login page
                .requestMatchers("/index.html").permitAll()
                .requestMatchers(
                    "/*.html","/*.css","/*.js",
                    "/favicon.ico","/assets/**"
                ).permitAll()

                // ── PROTECTED ──────────────────────────────────────────
                .requestMatchers("/api/hr/**")
                    .hasAnyRole("HR", "HR_ADMIN", "HR_MANAGER")

                // ── EVERYTHING ELSE needs auth ──────────────────────────
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

