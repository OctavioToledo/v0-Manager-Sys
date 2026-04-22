package com.demoV1Project.shared.security;

import com.demoV1Project.domain.model.User;
import com.demoV1Project.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class TenantContextTest {

    private UserRepository userRepository;
    private TenantContext tenantContext;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        tenantContext = new TenantContext(userRepository);
    }

    @Test
    void getCurrentUser_resolves_by_supabase_uid() {
        String uid = UUID.randomUUID().toString();
        User user = new User();
        user.setSupabaseUid(uid);

        mockJwtSub(uid);
        when(userRepository.findBySupabaseUid(uid)).thenReturn(Optional.of(user));

        User result = tenantContext.getCurrentUser();

        assertThat(result.getSupabaseUid()).isEqualTo(uid);
    }

    @Test
    void getCurrentUser_throws_when_user_not_found() {
        String uid = UUID.randomUUID().toString();
        mockJwtSub(uid);
        when(userRepository.findBySupabaseUid(uid)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tenantContext.getCurrentUser())
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("User not found");
    }

    private void mockJwtSub(String sub) {
        Jwt jwt = mock(Jwt.class);
        when(jwt.getSubject()).thenReturn(sub);
        Authentication auth = new JwtAuthenticationToken(jwt);
        SecurityContext ctx = mock(SecurityContext.class);
        when(ctx.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(ctx);
    }
}
