package com.demoV1Project.infrastructure.controller;

import com.demoV1Project.application.service.EmployeeWorkScheduleService;
import com.demoV1Project.domain.dto.EmployeeWorkScheduleDto.EmployeeWorkScheduleDto;

import com.demoV1Project.domain.dto.EmployeeWorkScheduleDto.EmployeeWorkScheduleRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v0/employees/{employeeId}/hours")
@RequiredArgsConstructor
public class EmployeeWorkScheduleController {
    private final EmployeeWorkScheduleService service;

    @PostMapping
    public ResponseEntity<List<EmployeeWorkScheduleDto>> saveAll(
            @PathVariable Long employeeId,
            @RequestBody List<EmployeeWorkScheduleRequest> requests
    ) {
        return ResponseEntity.ok(service.saveAllForEmployee(employeeId, requests));
    }

    @GetMapping
    public ResponseEntity<List<EmployeeWorkScheduleDto>> findByEmployeeId(
            @PathVariable Long employeeId
    ) {
        return ResponseEntity.ok(service.findByEmployeeId(employeeId));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteByEmployeeId(
            @PathVariable Long employeeId
    ) {
        service.deleteByEmployeeId(employeeId);
        return ResponseEntity.noContent().build();
    }
}