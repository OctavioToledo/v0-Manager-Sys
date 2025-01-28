package com.demoV1Project.infrastructure.controllers;

import com.demoV1Project.application.mapper.AppointmentMapper;
import com.demoV1Project.application.service.AppointmentService;
import com.demoV1Project.domain.dto.AppointmentDto;
import com.demoV1Project.domain.model.Appointment;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/v0/appointment")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final AppointmentMapper appointmentMapper;

    @GetMapping("/findAll")
    public ResponseEntity<List<AppointmentDto>> findAll() {
        List<Appointment> appointments = appointmentService.findAll();
        List<AppointmentDto> appointmentDtos = appointmentMapper.toDtoList(appointments);
        return ResponseEntity.ok(appointmentDtos);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<AppointmentDto> findById(@PathVariable Long id) {
        return appointmentService.findById(id)
                .map(appointmentMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody AppointmentDto appointmentDto) throws URISyntaxException {
        try {
            Appointment appointment = appointmentService.createAndSaveAppointment(appointmentDto);
            return ResponseEntity.created(new URI("/api/v0/appointment/save/"))
                    .body("Appointment created successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        if (appointmentService.findById(id).isPresent()) {
            appointmentService.deleteById(id);
            return ResponseEntity.ok("Appointment deleted successfully");
        }
        return ResponseEntity.badRequest().body("Invalid ID");
    }
}
