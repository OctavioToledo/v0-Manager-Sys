package com.demoV1Project.infrastructure.controllers;

import com.demoV1Project.application.service.AuthSyncService;
import com.demoV1Project.domain.dto.AuthDto.SyncResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthSyncControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthSyncService authSyncService;

    @Test
    void sync_returns_user_profile_when_business_exists() throws Exception {
        String uid = "test-uid-123";
        SyncResponse response = SyncResponse.builder()
            .id(1L)
            .supabaseUid(uid)
            .email("test@example.com")
            .name("Test User")
            .role("ADMIN")
            .businessId(5L)
            .build();

        when(authSyncService.syncUser(any(Jwt.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/sync")
                .with(jwt().jwt(j -> j.subject(uid).claim("email", "test@example.com"))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.supabaseUid").value(uid))
            .andExpect(jsonPath("$.email").value("test@example.com"))
            .andExpect(jsonPath("$.businessId").value(5L));
    }

    @Test
    void sync_returns_null_business_id_when_no_business() throws Exception {
        String uid = "new-uid-456";
        SyncResponse response = SyncResponse.builder()
            .id(99L)
            .supabaseUid(uid)
            .email("new@example.com")
            .name("new@example.com")
            .role("CLIENT")
            .businessId(null)
            .build();

        when(authSyncService.syncUser(any(Jwt.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/sync")
                .with(jwt().jwt(j -> j.subject(uid).claim("email", "new@example.com"))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.supabaseUid").value(uid))
            .andExpect(jsonPath("$.email").value("new@example.com"))
            .andExpect(jsonPath("$.businessId").doesNotExist());
    }
}
