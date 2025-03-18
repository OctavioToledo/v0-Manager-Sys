package com.demoV1Project.infrastructure.controllers;

import com.demoV1Project.application.mapper.AppointmentMapper;
import com.demoV1Project.application.service.AppointmentService;

import com.demoV1Project.domain.dto.AppointmentDto.AppointmentCreateDto;
import com.demoV1Project.domain.dto.AppointmentDto.AppointmentDto;
import com.demoV1Project.domain.dto.AppointmentDto.AppointmentUpdateDto;

import com.demoV1Project.domain.model.Appointment;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<String> save(@RequestBody AppointmentCreateDto appointmentCreateDto) throws URISyntaxException {
        try {
            Appointment appointment = appointmentService.createAndSaveAppointment(appointmentCreateDto);
            return ResponseEntity.created(new URI("/api/v0/appointment/save/"))
                    .body("Appointment created successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody AppointmentUpdateDto appointmentUpdateDto) {
        Optional<Appointment> appointmentOptional = appointmentService.findById(id);

        if (appointmentOptional.isPresent()) {
            Appointment appointment = appointmentOptional.get();
            appointmentMapper.updateEntity(appointmentUpdateDto, appointment); // Usa el mapper para actualizar
            appointmentService.save(appointment);
            return ResponseEntity.ok("Appointment updated successfully");
        }
        return ResponseEntity.badRequest().body("Appointment not found");
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
