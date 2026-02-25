package com.rspl.onboarding.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf().disable()
        .authorizeHttpRequests(authz -> authz
            // Allow public access to static resources and frontend
            .requestMatchers("/", "/index.html", "/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()
            // Allow public access to H2 console for development
            .requestMatchers("/h2/**").permitAll()
            // All API requests require authentication
            .requestMatchers("/api/**").authenticated()
            .anyRequest().permitAll()
        )
        .httpBasic()
        .and()
        .headers().frameOptions().disable();
    
    return http.build();
  }
}
