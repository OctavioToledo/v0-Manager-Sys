package com.demoV1Project.shared.security;

import com.demoV1Project.domain.model.Business;
import com.demoV1Project.domain.model.User;
import com.demoV1Project.domain.repository.BusinessRepository;
import com.demoV1Project.domain.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class TenantContext {

    private final UserRepository userRepository;
    private final BusinessRepository businessRepository;

    public TenantContext(UserRepository userRepository, BusinessRepository businessRepository) {
        this.userRepository = userRepository;
        this.businessRepository = businessRepository;
    }

    public User getCurrentUser() {
        String supabaseUid = extractSubFromJwt();
        return userRepository.findBySupabaseUid(supabaseUid)
            .orElseThrow(() -> new RuntimeException("User not found for uid: " + supabaseUid));
    }

    public String getCurrentSupabaseUid() {
        return extractSubFromJwt();
    }

    /**
     * Verifies that the currently authenticated user owns the given business.
     * Throws 403 if ownership cannot be confirmed.
     */
    public void validateBusinessOwnership(Long businessId) {
        User currentUser = getCurrentUser();
        Business business = businessRepository.findById(businessId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Business not found: " + businessId));
        if (!business.getUser().getId().equals(currentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied to business: " + businessId);
        }
    }

    private String extractSubFromJwt() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) auth.getPrincipal();
        return jwt.getSubject();
    }
}
