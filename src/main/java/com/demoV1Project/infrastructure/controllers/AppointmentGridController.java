package com.demoV1Project.infrastructure.controllers;

import com.demoV1Project.application.service.AppointmentService;
import com.demoV1Project.domain.dto.AppointmentGridDto.AppointmentGridDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v0/appointments")
@RequiredArgsConstructor
public class AppointmentGridController {

    @Autowired
    private final AppointmentService appointmentGridService;

    @GetMapping("/grid")
    public ResponseEntity<AppointmentGridDto> getAppointmentGrid(
            @RequestParam Long serviceId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        AppointmentGridDto grid = appointmentGridService.getAppointmentGrid(serviceId, date);
        return ResponseEntity.ok(grid);
    }
}
