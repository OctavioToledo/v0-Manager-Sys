package com.demoV1Project.application.service;

import com.demoV1Project.domain.dto.AuthDto.SyncResponse;
import org.springframework.security.oauth2.jwt.Jwt;

public interface AuthSyncService {
    SyncResponse syncUser(Jwt jwt);
}
