package com.demoV1Project.infrastructure.controllers;

import com.demoV1Project.domain.dto.BusinessHolidayDto.BusinessHolidayDto;
import com.demoV1Project.domain.model.Business;
import com.demoV1Project.domain.model.BusinessHoliday;
import com.demoV1Project.domain.repository.BusinessHolidayRepository;
import com.demoV1Project.domain.repository.BusinessRepository;
import com.demoV1Project.shared.security.TenantContext;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/business/{businessId}/config/holidays")
@RequiredArgsConstructor
public class BusinessHolidayController {

    private final BusinessHolidayRepository holidayRepository;
    private final BusinessRepository businessRepository;
    private final TenantContext tenantContext;

    @GetMapping
    public ResponseEntity<List<BusinessHolidayDto>> getHolidays(@PathVariable Long businessId) {
        tenantContext.validateBusinessOwnership(businessId);
        List<BusinessHolidayDto> result = holidayRepository
                .findByBusinessIdOrderByHolidayDateAsc(businessId)
                .stream()
                .map(this::toDto)
                .toList();
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<BusinessHolidayDto> addHoliday(
            @PathVariable Long businessId,
            @RequestBody BusinessHolidayDto dto) {
        tenantContext.validateBusinessOwnership(businessId);
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new EntityNotFoundException("Business not found: " + businessId));

        BusinessHoliday holiday = BusinessHoliday.builder()
                .holidayDate(LocalDate.parse(dto.getHolidayDate()))
                .description(dto.getDescription())
                .isFullDay(dto.getIsFullDay() != null ? dto.getIsFullDay() : true)
                .startTime(dto.getStartTime() != null ? LocalTime.parse(dto.getStartTime()) : null)
                .endTime(dto.getEndTime() != null ? LocalTime.parse(dto.getEndTime()) : null)
                .business(business)
                .build();

        BusinessHoliday saved = holidayRepository.save(holiday);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(saved));
    }

    @DeleteMapping("/{holidayId}")
    public ResponseEntity<Void> deleteHoliday(
            @PathVariable Long businessId,
            @PathVariable Long holidayId) {
        tenantContext.validateBusinessOwnership(businessId);
        BusinessHoliday holiday = holidayRepository.findById(holidayId)
                .orElseThrow(() -> new EntityNotFoundException("Holiday not found: " + holidayId));
        if (!holiday.getBusiness().getId().equals(businessId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        holidayRepository.deleteById(holidayId);
        return ResponseEntity.noContent().build();
    }

    private BusinessHolidayDto toDto(BusinessHoliday h) {
        return BusinessHolidayDto.builder()
                .id(h.getId())
                .holidayDate(h.getHolidayDate().toString())
                .description(h.getDescription())
                .isFullDay(h.getIsFullDay())
                .startTime(h.getStartTime() != null ? h.getStartTime().toString() : null)
                .endTime(h.getEndTime() != null ? h.getEndTime().toString() : null)
                .build();
    }
}
