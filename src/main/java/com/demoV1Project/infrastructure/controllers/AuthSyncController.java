package com.demoV1Project.infrastructure.controllers;

import com.demoV1Project.domain.dto.AuthDto.SyncResponse;
import com.demoV1Project.domain.model.User;
import com.demoV1Project.domain.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthSyncController {

    private final UserRepository userRepository;

    public AuthSyncController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/sync")
    public ResponseEntity<SyncResponse> sync(@AuthenticationPrincipal Jwt jwt) {
        String supabaseUid = jwt.getSubject();
        String email = jwt.getClaimAsString("email");

        User user = userRepository.findBySupabaseUid(supabaseUid)
            .orElseGet(() -> {
                User newUser = new User();
                newUser.setSupabaseUid(supabaseUid);
                newUser.setEmail(email);
                newUser.setName(email);
                // Default role for new users registering via Supabase Auth
                newUser.setRole(com.demoV1Project.util.enums.Role.CLIENT);
                return userRepository.save(newUser);
            });

        return ResponseEntity.ok(SyncResponse.builder()
            .id(user.getId())
            .supabaseUid(user.getSupabaseUid())
            .email(user.getEmail())
            .name(user.getName())
            .role(user.getRole() != null ? user.getRole().toString() : "CLIENT")
            .build());
    }
}
