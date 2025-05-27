package com.demoV1Project.infrastructure.controllers;

import com.demoV1Project.application.service.BusinessRegistrationService;
import com.demoV1Project.domain.dto.BusinessDto.BusinessDto;
import com.demoV1Project.domain.dto.BusinessDto.BusinessRegistrationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v0/business-registration")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class BusinessRegistrationController {

    private final BusinessRegistrationService businessRegistrationService;

    @PostMapping("/register")
    public ResponseEntity<Long> registerBusiness(@RequestBody BusinessRegistrationDto registrationDto) {
        BusinessDto createdBusiness = businessRegistrationService.registerBusiness(registrationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBusiness.getId());
    }
}

