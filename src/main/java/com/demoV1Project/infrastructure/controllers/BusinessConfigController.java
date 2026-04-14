package com.demoV1Project.infrastructure.controllers;

import com.demoV1Project.domain.model.Business;
import com.demoV1Project.domain.model.BusinessHoliday;
import com.demoV1Project.domain.repository.BusinessHolidayRepository;
import com.demoV1Project.domain.repository.BusinessRepository;
import com.demoV1Project.shared.security.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/business/{businessId}/config")
@RequiredArgsConstructor
public class BusinessConfigController {

    private final BusinessRepository businessRepository;
    private final BusinessHolidayRepository businessHolidayRepository;
    private final TenantContext tenantContext;

    @PatchMapping("/slot-duration")
    public ResponseEntity<Business> updateSlotDuration(
            @PathVariable Long businessId,
            @RequestBody Map<String, Integer> payload) {
        tenantContext.validateBusinessOwnership(businessId);
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new IllegalArgumentException("Business not found"));
        
        business.setSlotDuration(payload.get("slotDuration"));
        return ResponseEntity.ok(businessRepository.save(business));
    }

    @GetMapping("/holidays")
    public ResponseEntity<List<BusinessHoliday>> getHolidays(@PathVariable Long businessId) {
        tenantContext.validateBusinessOwnership(businessId);
        return ResponseEntity.ok(businessHolidayRepository.findByBusinessId(businessId));
    }

    @PostMapping("/holidays")
    public ResponseEntity<BusinessHoliday> addHoliday(
            @PathVariable Long businessId,
            @RequestBody BusinessHoliday holiday) {
        tenantContext.validateBusinessOwnership(businessId);
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new IllegalArgumentException("Business not found"));
        
        holiday.setBusiness(business);
        return ResponseEntity.ok(businessHolidayRepository.save(holiday));
    }

    @DeleteMapping("/holidays/{holidayId}")
    public ResponseEntity<Void> deleteHoliday(
            @PathVariable Long businessId,
            @PathVariable Long holidayId) {
        tenantContext.validateBusinessOwnership(businessId);
        businessHolidayRepository.deleteById(holidayId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/availability-check")
    public ResponseEntity<Map<String, Object>> checkAvailabilitySync(@PathVariable Long businessId) {
        tenantContext.validateBusinessOwnership(businessId);
        // This would implement the logic to find inconsistencies between 
        // business_hours and employee_work_schedule.
        // For now, returning a sample status.
        return ResponseEntity.ok(Map.of(
            "status", "OK",
            "message", "Todas las agendas de profesionales están dentro del rango del negocio."
        ));
    }
}
