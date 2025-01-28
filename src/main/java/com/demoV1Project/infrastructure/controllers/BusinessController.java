package com.demoV1Project.infrastructure.controllers;

import com.demoV1Project.application.mapper.BusinessMapper;
import com.demoV1Project.application.service.BusinessService;
import com.demoV1Project.domain.dto.BusinessDto;
import com.demoV1Project.domain.model.Business;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v0/business")
@RequiredArgsConstructor
public class BusinessController {

    @Autowired
    private final BusinessService businessService;

    @Autowired
    private final BusinessMapper businessMapper;

    @GetMapping("/findAll")
    public ResponseEntity<?> findAll() {
        List<BusinessDto> businessList = businessService.findAll().stream()
                .map(businessMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(businessList);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return businessService.findById(id)
                .map(business -> ResponseEntity.ok(businessMapper.toDto(business)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody BusinessDto businessDto) throws URISyntaxException {
        Business business = businessMapper.toEntity(businessDto);
        businessService.save(business);
        return ResponseEntity.created(new URI("/api/v0/business/save/")).body("Business created successfully");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody BusinessDto businessDto) {
        Business business = businessService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Business not found"));

        // Actualización de los campos restantes
        business.setName(businessDto.getName());
        business.setDescription(businessDto.getDescription());
        business.setPhoneNumber(businessDto.getPhoneNumber());
        business.setLogo(businessDto.getLogo());
        business.setOpeningHours(businessDto.getOpeningHours());
        business.setWorkDays(businessDto.getWorkDays());
        business.setUser(businessDto.getUser());
        business.setCategory(businessDto.getCategory());
        business.setAddress(businessDto.getAddress());
        businessService.save(business);

        return ResponseEntity.ok("Business updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        if (id != null) {
            businessService.deleteById(id);
            return ResponseEntity.ok("Field Deleted");
        }
        return ResponseEntity.badRequest().build();
    }
}
