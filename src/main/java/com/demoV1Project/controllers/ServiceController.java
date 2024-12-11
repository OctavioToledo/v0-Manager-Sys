package com.demoV1Project.controllers;

import com.demoV1Project.dto.ServiceDto;
import com.demoV1Project.model.Business;
import com.demoV1Project.model.Employee;
import com.demoV1Project.model.Service;
import com.demoV1Project.service.BusinessService;
import com.demoV1Project.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v0/service")
@RequiredArgsConstructor
public class ServiceController {

    @Autowired
    private final ServiceService serviceService;

    @Autowired
    private final BusinessService businessService;

    @GetMapping("/findAll")
    public ResponseEntity<List<ServiceDto>> findAll() {
        List<ServiceDto> services = serviceService.findAll()
                .stream()
                .map(service -> ServiceDto.builder()
                        .id(service.getId())
                        .name(service.getName())
                        .duration(service.getDuration())
                        .description(service.getDescription())
                        .price(service.getPrice())
                        .businessId(service.getBusiness().getId())
                        .employeeIds(service.getEmployees()
                                .stream()
                                .map(Employee::getId)
                                .toList())
                        .build())
                .toList();
        return ResponseEntity.ok(services);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<Service> serviceOptional = serviceService.findById(id);
        if (serviceOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Service not found");
        }

        Service service = serviceOptional.get();
        ServiceDto serviceDto = ServiceDto.builder()
                .id(service.getId())
                .name(service.getName())
                .duration(service.getDuration())
                .description(service.getDescription())
                .price(service.getPrice())
                .businessId(service.getBusiness().getId())
                .employeeIds(service.getEmployees()
                        .stream()
                        .map(Employee::getId)
                        .toList())
                .build();
        return ResponseEntity.ok(serviceDto);
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody ServiceDto serviceDto) {
        // Validaciones de negocio según sea necesario

        if (serviceDto.getBusinessId() == null) {
            return ResponseEntity.badRequest().body("Business ID is required");
        }

        // Buscar el negocio correspondiente
        Optional<Business> businessOptional = businessService.findById(serviceDto.getBusinessId());
        if (businessOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Business not found");
        }

        Business business = businessOptional.get();

        Service service = Service.builder()
                .name(serviceDto.getName())
                .duration(serviceDto.getDuration())
                .description(serviceDto.getDescription())
                .price(serviceDto.getPrice())
                .business(business)
                .build();
        serviceService.save(service);
        return ResponseEntity.status(HttpStatus.CREATED).body("Service saved successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        serviceService.deleteById(id);
        return ResponseEntity.ok("Service deleted successfully");
    }

}
