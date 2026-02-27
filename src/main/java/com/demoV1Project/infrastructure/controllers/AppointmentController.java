package com.demoV1Project.infrastructure.controllers;

import com.demoV1Project.application.mapper.AppointmentMapper;
import com.demoV1Project.application.service.AppointmentService;

import com.demoV1Project.domain.dto.AppointmentDto.AppointmentCreateDto;
import com.demoV1Project.domain.dto.AppointmentDto.AppointmentDto;
import com.demoV1Project.domain.dto.AppointmentDto.AppointmentUpdateDto;

import com.demoV1Project.domain.model.Appointment;
import com.demoV1Project.shared.security.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/appointment")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final AppointmentMapper appointmentMapper;
    private final TenantContext tenantContext;

    @GetMapping("/findAll")
    public ResponseEntity<org.springframework.data.domain.Page<AppointmentDto>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(
                page, size, org.springframework.data.domain.Sort.by("createdAt").descending());
        org.springframework.data.domain.Page<AppointmentDto> result = appointmentService.findAll(pageable)
                .map(appointmentMapper::toDto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<AppointmentDto> findById(@PathVariable Long id) {
        return appointmentService.findById(id)
                .map(appointment -> {
                    tenantContext.validateBusinessOwnership(appointment.getBusiness().getId());
                    return ResponseEntity.ok(appointmentMapper.toDto(appointment));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody AppointmentCreateDto appointmentCreateDto) throws URISyntaxException {
        tenantContext.validateBusinessOwnership(appointmentCreateDto.getBusinessId());
        Appointment appointment = appointmentService.createAndSaveAppointment(appointmentCreateDto);
        return ResponseEntity.created(new URI("/api/v1/appointment/save/"))
                .body(appointment.getId());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable Long id,
            @RequestBody AppointmentUpdateDto appointmentUpdateDto) {
        Appointment appointment = appointmentService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));
        tenantContext.validateBusinessOwnership(appointment.getBusiness().getId());
        appointmentMapper.updateEntity(appointmentUpdateDto, appointment);
        appointmentService.save(appointment);
        return ResponseEntity.ok("Appointment updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        Appointment appointment = appointmentService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));
        tenantContext.validateBusinessOwnership(appointment.getBusiness().getId());
        appointmentService.deleteById(id);
        return ResponseEntity.ok("Appointment deleted successfully");
    }
}
