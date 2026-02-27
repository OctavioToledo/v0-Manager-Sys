package com.demoV1Project.infrastructure.controllers;

import com.demoV1Project.application.service.BusinessHoursService;
import com.demoV1Project.domain.dto.BusinessHours.BusinessHoursDto;
import com.demoV1Project.shared.security.TenantContext;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/business/{businessId}/hours")
@RequiredArgsConstructor
public class BusinessHoursController {
    private final BusinessHoursService businessHoursService;
    private final TenantContext tenantContext;

    @PostMapping
    public ResponseEntity<List<BusinessHoursDto>> saveHours(
            @PathVariable Long businessId,
            @Valid @RequestBody List<BusinessHoursDto> requests) {
        tenantContext.validateBusinessOwnership(businessId);
        return ResponseEntity.ok(
                businessHoursService.saveHoursForBusiness(businessId, requests));
    }

    @GetMapping
    public ResponseEntity<List<BusinessHoursDto>> getHours(@PathVariable Long businessId) {
        tenantContext.validateBusinessOwnership(businessId);
        return ResponseEntity.ok(businessHoursService.findByBusinessId(businessId));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteHours(@PathVariable Long businessId) {
        tenantContext.validateBusinessOwnership(businessId);
        businessHoursService.deleteByBusinessId(businessId);
        return ResponseEntity.noContent().build();
    }
}
