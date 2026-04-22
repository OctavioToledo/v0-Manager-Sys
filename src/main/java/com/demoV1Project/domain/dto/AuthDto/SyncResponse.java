package com.demoV1Project.domain.dto.AuthDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SyncResponse {
    private Long id;
    private String supabaseUid;
    private String email;
    private String name;
    private String role;
    private Long businessId;   // null si el usuario no tiene negocio registrado
}
