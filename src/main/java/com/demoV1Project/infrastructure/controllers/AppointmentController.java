package com.demoV1Project.infrastructure.controllers;

import com.demoV1Project.application.mapper.AppointmentMapper;
import com.demoV1Project.application.service.AppointmentService;
import com.demoV1Project.domain.dto.AppointmentDto.AppointmentCreateDto;
import com.demoV1Project.domain.dto.AppointmentDto.AppointmentDto;
import com.demoV1Project.domain.dto.AppointmentDto.AppointmentUpdateDto;
import com.demoV1Project.domain.model.Appointment;
import com.demoV1Project.shared.security.TenantContext;
import com.demoV1Project.util.enums.AppointmentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.Optional;

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

    /**
     * POST /request
     * El cliente selecciona un slot. Se crea PENDING, esperando la aprobación del
     * negocio.
     */
    @PostMapping("/request")
    public ResponseEntity<?> requestAppointment(@RequestBody AppointmentCreateDto dto) throws URISyntaxException {
        // Validación básica de solapamiento PREVIO a insertar
        // Notice we don't have endTime directly available since client just sends
        // startTime,
        // but the service handles exact times anyway. We can pass a dummy endTime or
        // calculate it inside the service. For now, checkCollision uses the DTO's
        // startTime.

        // Emplearemos el método createPendingAppointment que valida TODO (horarios de
        // negocio y overlapping general)
        try {
            Appointment appointment = appointmentService.createPendingAppointment(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(appointmentMapper.toDto(appointment));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    /**
     * PUT /{id}/approve
     * El administrador acepta el turno. SE VERIFICA COINCIDENCIA CRÍTICA.
     */
    @PutMapping("/{id}/approve")
    public ResponseEntity<?> approveAppointment(@PathVariable Long id) {
        Optional<Appointment> appointmentOpt = appointmentService.findById(id);
        if (appointmentOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Turno no encontrado");
        }

        Appointment appointment = appointmentOpt.get();
        tenantContext.validateBusinessOwnership(appointment.getBusiness().getId());

        if (appointment.getStatus() != AppointmentStatus.PENDING) {
            return ResponseEntity.badRequest().body("El turno no está en estado PENDING.");
        }

        // Anti-Colisión Definitiva (Race Condition)
        boolean collisionWithApproved = appointmentService.checkCollisionWithApproved(
                appointment.getEmployee().getId(),
                appointment.getAppointmentDate(),
                appointment.getStartTime(),
                appointment.getEndTime());

        if (collisionWithApproved) {
            // El slot ya fue ganado por otro
            appointment.setStatus(AppointmentStatus.REJECTED);
            appointmentService.save(appointment);
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("El slot fue ocupado por otro turno aprobado recientemente.");
        }

        // Si pasa, se aprueba oficialmente
        appointment.setStatus(AppointmentStatus.APPROVED);
        appointmentService.save(appointment);

        return ResponseEntity.ok(appointmentMapper.toDto(appointment));
    }

    /**
     * PUT /{id}/reject
     * Cancela el slot PENDING y lo libera devuelta a la grilla pública.
     */
    @PutMapping("/{id}/reject")
    public ResponseEntity<?> rejectAppointment(@PathVariable Long id) {
        Optional<Appointment> appointmentOpt = appointmentService.findById(id);
        if (appointmentOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Turno no encontrado");
        }

        Appointment appointment = appointmentOpt.get();
        tenantContext.validateBusinessOwnership(appointment.getBusiness().getId());

        if (appointment.getStatus() != AppointmentStatus.PENDING
                && appointment.getStatus() != AppointmentStatus.APPROVED) {
            return ResponseEntity.badRequest().body("No es posible rechazar el turno en su estado actual.");
        }

        appointment.setStatus(AppointmentStatus.REJECTED);
        appointmentService.save(appointment);

        return ResponseEntity.ok("Turno rechazado y slot liberado correctamente.");
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
