package com.demoV1Project.infrastructure.controllers;

import com.demoV1Project.application.mapper.BusinessMapper;
import com.demoV1Project.application.service.BusinessService;
import com.demoV1Project.application.service.CategoryService;
import com.demoV1Project.domain.dto.BusinessDto.*;
import com.demoV1Project.domain.model.Business;
import com.demoV1Project.domain.model.Category;
import com.demoV1Project.shared.security.TenantContext;
import lombok.RequiredArgsConstructor;
import com.demoV1Project.config.security.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/business")
@RequiredArgsConstructor
public class BusinessController {

    private final BusinessService businessService;
    private final BusinessMapper businessMapper;
    private final CategoryService categoryService;
    private final TenantContext tenantContext;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/findAll")
    public ResponseEntity<org.springframework.data.domain.Page<BusinessDto>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(
                page, size, org.springframework.data.domain.Sort.by("createdAt").descending());
        org.springframework.data.domain.Page<BusinessDto> result = businessService.findAll(pageable)
                .map(businessMapper::toDto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<BusinessDetailDto> findById(@PathVariable Long id) {
        return businessService.findById(id)
                .map(business -> {
                    tenantContext.validateBusinessOwnership(business.getId());
                    return ResponseEntity.ok(businessMapper.toDetailDto(business));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/findShort/{id}")
    public ResponseEntity<BusinessShortDto> findShortById(@PathVariable Long id) {
        return businessService.findById(id)
                .map(business -> {
                    return ResponseEntity.ok(businessMapper.toShortDto(business));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<BusinessShortDto>> searchBusinesses(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String city) {

        List<BusinessShortDto> results = businessService.searchBusinesses(name, category, city);

        return ResponseEntity.ok(results);
    }

    @PostMapping("/save")
    public ResponseEntity<Map<String, Object>> save(@RequestBody BusinessCreateDto businessCreateDto)
            throws URISyntaxException {
        Business business = businessMapper.toEntity(businessCreateDto);
        // Asignar el usuario autenticado como dueño
        business.setUser(tenantContext.getCurrentUser());

        // Ensure Address has a reference back to Business for bidirectional persistence
        if (business.getAddress() != null) {
            business.getAddress().setBusiness(business);
        }

        // Cargar categoria real para evitar errores de entidad desatada
        if (business.getCategory() != null && business.getCategory().getId() != null) {
            Category category = categoryService.findById(business.getCategory().getId()).orElse(null);
            business.setCategory(category);
        }

        Business savedBusiness = businessService.save(business);

        String newToken = jwtTokenProvider.generateToken(
                tenantContext.getCurrentUser().getId(),
                tenantContext.getCurrentUser().getEmail(),
                tenantContext.getCurrentUser().getRole().name(),
                savedBusiness.getId());

        return ResponseEntity.created(new URI("/api/v1/business/save/"))
                .body(Map.of(
                        "business", savedBusiness.getId(),
                        "token", newToken));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody BusinessUpdateDto businessUpdateDto) {
        Business business = businessService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Business not found"));
        tenantContext.validateBusinessOwnership(business.getId());
        businessMapper.updateEntity(businessUpdateDto, business);
        businessService.save(business);
        return ResponseEntity.ok("Business updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        tenantContext.validateBusinessOwnership(id);
        businessService.deleteById(id);
        return ResponseEntity.ok("Business deleted successfully");
    }
}
