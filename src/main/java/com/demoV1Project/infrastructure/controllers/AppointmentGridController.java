package com.demoV1Project.infrastructure.controllers;

import com.demoV1Project.application.service.AppointmentService;
import com.demoV1Project.domain.dto.AppointmentGridDto.AppointmentGridDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v0/appointments")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class AppointmentGridController {

    @Autowired
    private final AppointmentService appointmentGridService;

    @GetMapping("/grid")
    public ResponseEntity<AppointmentGridDto> getAppointmentGrid(
            @RequestParam Long serviceId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date
    ) {
        AppointmentGridDto grid = appointmentGridService.getAppointmentGrid(serviceId, date);
        return ResponseEntity.ok(grid);
    }
}
