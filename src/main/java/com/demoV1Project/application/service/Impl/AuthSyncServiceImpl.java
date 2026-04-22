package com.demoV1Project.application.service.Impl;

import com.demoV1Project.application.service.AuthSyncService;
import com.demoV1Project.domain.dto.AuthDto.SyncResponse;
import com.demoV1Project.domain.model.Business;
import com.demoV1Project.domain.model.User;
import com.demoV1Project.domain.repository.BusinessRepository;
import com.demoV1Project.domain.repository.UserRepository;
import com.demoV1Project.util.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthSyncServiceImpl implements AuthSyncService {

    private final UserRepository userRepository;
    private final BusinessRepository businessRepository;

    @Override
    public SyncResponse syncUser(Jwt jwt) {
        String supabaseUid = jwt.getSubject();
        String email = jwt.getClaimAsString("email");

        User user = userRepository.findBySupabaseUid(supabaseUid)
            .orElseGet(() -> {
                User newUser = new User();
                newUser.setSupabaseUid(supabaseUid);
                newUser.setEmail(email);
                newUser.setName(email);
                newUser.setRole(Role.CLIENT);
                return userRepository.save(newUser);
            });

        Long businessId = businessRepository.findByUserId(user.getId())
            .map(Business::getId)
            .orElse(null);

        return SyncResponse.builder()
            .id(user.getId())
            .supabaseUid(user.getSupabaseUid())
            .email(user.getEmail())
            .name(user.getName())
            .role(user.getRole() != null ? user.getRole().toString() : "CLIENT")
            .businessId(businessId)
            .build();
    }
}
