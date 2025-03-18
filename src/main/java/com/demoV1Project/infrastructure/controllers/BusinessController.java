package com.demoV1Project.infrastructure.controllers;

import com.demoV1Project.application.mapper.BusinessMapper;
import com.demoV1Project.application.service.BusinessService;
import com.demoV1Project.domain.dto.BusinessDto.*;
import com.demoV1Project.domain.model.Address;
import com.demoV1Project.domain.model.Business;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/v0/business")
@RequiredArgsConstructor
public class BusinessController {

    @Autowired
    private final BusinessService businessService;

    @Autowired
    private final BusinessMapper businessMapper;

    @GetMapping("/findAll")
    public ResponseEntity<List<BusinessDto>> findAll() {
        List<Business> businesses = businessService.findAll();
        List<BusinessDto> businessDtos = businessMapper.toDtoList(businesses);
        return ResponseEntity.ok(businessDtos);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<BusinessDetailDto> findById(@PathVariable Long id) {
        return businessService.findById(id)
                .map(business -> ResponseEntity.ok(businessMapper.toDetailDto(business)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/findShort/{id}")
    public ResponseEntity<BusinessShortDto> findShortById(@PathVariable Long id) {
        return businessService.findById(id)
                .map(business -> ResponseEntity.ok(businessMapper.toShortDto(business)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody BusinessCreateDto businessCreateDto) throws URISyntaxException {
        Business business = businessMapper.toEntity(businessCreateDto);
        businessService.save(business);
        return ResponseEntity.created(new URI("/api/v0/business/save/")).body("Business created successfully");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody BusinessUpdateDto businessUpdateDto) {
        Business business = businessService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Business not found"));
        businessService.save(business);
        businessMapper.updateEntity(businessUpdateDto, business); // Usa el mapper para actualizar
        return ResponseEntity.ok("Business updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        businessService.deleteById(id);
        return ResponseEntity.ok("Business deleted successfully");
    }
}
