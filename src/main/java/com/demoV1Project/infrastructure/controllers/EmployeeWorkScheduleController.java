package com.demoV1Project.infrastructure.controllers;

import com.demoV1Project.application.service.EmployeeService;
import com.demoV1Project.application.service.EmployeeWorkScheduleService;
import com.demoV1Project.domain.dto.EmployeeWorkScheduleDto.EmployeeWorkScheduleDto;

import com.demoV1Project.domain.dto.EmployeeWorkScheduleDto.EmployeeWorkScheduleRequest;
import com.demoV1Project.domain.model.Employee;
import com.demoV1Project.shared.security.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees/{employeeId}/hours")
@RequiredArgsConstructor
public class EmployeeWorkScheduleController {
    private final EmployeeWorkScheduleService service;
    private final EmployeeService employeeService;
    private final TenantContext tenantContext;

    @PostMapping
    public ResponseEntity<List<EmployeeWorkScheduleDto>> saveAll(
            @PathVariable Long employeeId,
            @RequestBody List<EmployeeWorkScheduleRequest> requests) {
        validateEmployeeOwnership(employeeId);
        return ResponseEntity.ok(service.saveAllForEmployee(employeeId, requests));
    }

    @GetMapping("")
    public ResponseEntity<List<EmployeeWorkScheduleDto>> findByEmployeeId(
            @PathVariable Long employeeId) {
        validateEmployeeOwnership(employeeId);
        return ResponseEntity.ok(service.findByEmployeeId(employeeId));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteByEmployeeId(
            @PathVariable Long employeeId) {
        validateEmployeeOwnership(employeeId);
        service.deleteByEmployeeId(employeeId);
        return ResponseEntity.noContent().build();
    }

    private void validateEmployeeOwnership(Long employeeId) {
        Employee employee = employeeService.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));
        tenantContext.validateBusinessOwnership(employee.getBusiness().getId());
    }
}