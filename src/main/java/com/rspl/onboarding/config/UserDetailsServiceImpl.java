package com.rspl.onboarding.config;

import com.rspl.onboarding.auth.HrUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Primary
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final HrUserService hrUserService;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        // ✅ Load from HrUserService (all 6 in-memory users)
        Map<String, String> user = hrUserService.getUser(username.toLowerCase());

        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        return User.builder()
                .username(username.toLowerCase())
                .password("{noop}" + user.get("password")) // ✅ plain text, no BCrypt
                .authorities(List.of(
                    new SimpleGrantedAuthority("ROLE_" + user.get("role"))
                ))
                .build();
    }
}
