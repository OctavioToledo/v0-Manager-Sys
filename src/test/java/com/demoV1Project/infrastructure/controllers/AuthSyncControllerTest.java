package com.demoV1Project.infrastructure.controllers;

import com.demoV1Project.domain.model.User;
import com.demoV1Project.domain.repository.UserRepository;
import com.demoV1Project.util.enums.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthSyncControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @Test
    void sync_returns_user_profile_when_exists() throws Exception {
        String uid = UUID.randomUUID().toString();
        User user = new User();
        user.setId(1L);
        user.setSupabaseUid(uid);
        user.setEmail("test@example.com");
        user.setName("Test User");
        user.setRole(Role.CLIENT);

        when(userRepository.findBySupabaseUid(uid)).thenReturn(Optional.of(user));

        mockMvc.perform(post("/api/v1/auth/sync")
                .with(jwt().jwt(j -> j.subject(uid).claim("email", "test@example.com"))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.supabaseUid").value(uid))
            .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void sync_creates_user_when_not_found() throws Exception {
        String uid = UUID.randomUUID().toString();

        when(userRepository.findBySupabaseUid(uid)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setId(99L);
            return u;
        });

        mockMvc.perform(post("/api/v1/auth/sync")
                .with(jwt().jwt(j -> j.subject(uid).claim("email", "new@example.com"))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.supabaseUid").value(uid))
            .andExpect(jsonPath("$.email").value("new@example.com"));
    }
}
