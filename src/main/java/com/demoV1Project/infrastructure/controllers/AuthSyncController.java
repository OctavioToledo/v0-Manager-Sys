package com.demoV1Project.infrastructure.controllers;

import com.demoV1Project.application.service.AuthSyncService;
import com.demoV1Project.domain.dto.AuthDto.SyncResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthSyncController {

    private final AuthSyncService authSyncService;

    @PostMapping("/sync")
    public ResponseEntity<SyncResponse> sync(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(authSyncService.syncUser(jwt));
    }
}
