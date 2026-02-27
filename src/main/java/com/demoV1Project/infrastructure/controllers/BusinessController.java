package com.demoV1Project.infrastructure.controllers;

import com.demoV1Project.application.mapper.BusinessMapper;
import com.demoV1Project.application.service.BusinessService;
import com.demoV1Project.application.service.CategoryService;
import com.demoV1Project.domain.dto.BusinessDto.*;
import com.demoV1Project.domain.model.Business;
import com.demoV1Project.domain.model.Category;
import com.demoV1Project.shared.security.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/business")
@RequiredArgsConstructor
public class BusinessController {

    private final BusinessService businessService;
    private final BusinessMapper businessMapper;
    private final CategoryService categoryService;
    private final TenantContext tenantContext;

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
                    tenantContext.validateBusinessOwnership(business.getId());
                    return ResponseEntity.ok(businessMapper.toShortDto(business));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<BusinessShortDto>> searchBusinesses(
            @RequestParam(required = false) String name,
            @RequestParam(required = true) String category,
            @RequestParam(required = true) String city) {

        Category categoryObj = null;
        if (category != null && !category.isEmpty()) {
            categoryObj = categoryService.findByName(category).orElse(null);
        }

        List<BusinessShortDto> results = businessService.searchBusinesses(name, categoryObj, city);

        return ResponseEntity.ok(results);
    }

    @PostMapping("/save")
    public ResponseEntity<Long> save(@RequestBody BusinessCreateDto businessCreateDto) throws URISyntaxException {
        Business business = businessMapper.toEntity(businessCreateDto);
        // Asignar el usuario autenticado como dueño
        business.setUser(tenantContext.getCurrentUser());
        Business savedBusiness = businessService.save(business);
        return ResponseEntity.created(new URI("/api/v1/business/save/"))
                .body(savedBusiness.getId());
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
