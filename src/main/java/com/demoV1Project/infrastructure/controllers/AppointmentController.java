package com.demoV1Project.infrastructure.controllers;

import com.demoV1Project.application.mapper.AppointmentMapper;
import com.demoV1Project.domain.dto.AppointmentDto;
import com.demoV1Project.util.enums.AppointmentStatus;
import com.demoV1Project.domain.model.Appointment;
import com.demoV1Project.domain.model.Employee;
import com.demoV1Project.domain.model.Service;
import com.demoV1Project.domain.model.User;
import com.demoV1Project.application.service.AppointmentService;
import com.demoV1Project.application.service.EmployeeService;
import com.demoV1Project.application.service.ServiceService;
import com.demoV1Project.application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private final AppointmentService appointmentService;

    @Autowired
    private final UserService userService;

    @Autowired
    private final EmployeeService employeeService;

    @Autowired
    private final ServiceService serviceService;

    @Autowired
    private final AppointmentMapper appointmentMapper;
    
    @GetMapping("/findAll")
    public ResponseEntity<?> findAll() {
        List<AppointmentDto> appointmentList = appointmentService.findAll()
                .stream()
                .map(appointmentMapper::toDto)
                .toList();

        return ResponseEntity.ok(appointmentList);
    }


    @GetMapping("/find/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return appointmentService.findById(id)
                .map(appointmentMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody AppointmentDto appointmentDto) {
        try {
            Appointment appointment = appointmentService.createAndSaveAppointment(appointmentDto);
            return ResponseEntity.ok(appointment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
        public ResponseEntity<?> deleteById(@PathVariable Long id) {
            if (id != null) {
                appointmentService.deleteById(id);
                return ResponseEntity.ok("Appointment Deleted");
            }
            return ResponseEntity.badRequest().build();
        }
    }

