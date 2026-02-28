package com.rspl.onboarding.config;

import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Primary
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if ("hr@rspl.com".equals(username)) {
            return User.builder()
                .username("hr@rspl.com")
                .password("{noop}password123")  // plain text for dev
                .roles("HR")
                .build();
        }
        throw new UsernameNotFoundException("User not found: " + username);
    }
}
