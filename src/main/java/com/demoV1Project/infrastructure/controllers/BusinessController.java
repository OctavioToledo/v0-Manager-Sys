package com.demoV1Project.infrastructure.controllers;

import com.demoV1Project.application.mapper.BusinessMapper;
import com.demoV1Project.application.service.BusinessService;
import com.demoV1Project.domain.dto.BusinessDto.BusinessDto;
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
    public ResponseEntity<BusinessDto> findById(@PathVariable Long id) {
        return businessService.findById(id)
                .map(business -> ResponseEntity.ok(businessMapper.toDto(business)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody BusinessDto businessDto) throws URISyntaxException {
        Business business = businessMapper.toEntity(businessDto);
        businessService.save(business);
        return ResponseEntity.created(new URI("/api/v0/business/save/")).body("Business created successfully");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody BusinessDto businessDto) {
        Business business = businessService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Business not found"));

        // Actualización del negocio usando los datos del DTO
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
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        businessService.deleteById(id);
        return ResponseEntity.ok("Business deleted successfully");
    }
}
