package com.demoV1Project.shared.security;

import com.demoV1Project.domain.model.User;
import com.demoV1Project.domain.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class TenantContext {

    private final UserRepository userRepository;

    public TenantContext(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentUser() {
        String supabaseUid = extractSubFromJwt();
        return userRepository.findBySupabaseUid(supabaseUid)
            .orElseThrow(() -> new RuntimeException("User not found for uid: " + supabaseUid));
    }

    public String getCurrentSupabaseUid() {
        return extractSubFromJwt();
    }

    private String extractSubFromJwt() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) auth.getPrincipal();
        return jwt.getSubject();
    }
}
