package com.demoV1Project.infrastructure.controllers;

import com.demoV1Project.application.mapper.ServiceMapper;
import com.demoV1Project.application.service.BusinessService;
import com.demoV1Project.application.service.ServiceService;
import com.demoV1Project.domain.dto.EmployeeDto.EmployeeUpdateDto;
import com.demoV1Project.domain.dto.ServiceDto.ServiceCreateDto;
import com.demoV1Project.domain.dto.ServiceDto.ServiceDto;
import com.demoV1Project.domain.dto.ServiceDto.ServiceUpdateDto;
import com.demoV1Project.domain.model.Employee;
import com.demoV1Project.domain.model.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v0/service")
@RequiredArgsConstructor
public class ServiceController {

    private final ServiceService serviceService;
    private final BusinessService businessService;
    private final ServiceMapper serviceMapper;

    @GetMapping("/findAll")
    public ResponseEntity<List<ServiceDto>> findAll() {
        List<Service> services = serviceService.findAll();
        return ResponseEntity.ok(serviceMapper.toDtoList(services));
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<ServiceDto> findById(@PathVariable Long id) {
        return serviceService.findById(id)
                .map(serviceMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody ServiceCreateDto serviceCreateDto) {
        if (serviceCreateDto.getBusinessId() == null) {
            return ResponseEntity.badRequest().body("Business ID is required");
        }
        return businessService.findById(serviceCreateDto.getBusinessId())
                .map(business -> {
                    Service service = serviceMapper.toEntity(serviceCreateDto);
                    service.setBusiness(business);
                    serviceService.save(service);
                    return ResponseEntity.status(HttpStatus.CREATED).body("Service saved successfully");
                })
                .orElse(ResponseEntity.badRequest().body("Business not found"));
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody ServiceUpdateDto serviceUpdateDto){
        Service service = serviceService.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Service Not Found"));
        serviceMapper.updateEntity(serviceUpdateDto, service);
        serviceService.save(service);
        return ResponseEntity.ok("Service Updated Successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        if (serviceService.findById(id).isPresent()) {
            serviceService.deleteById(id);
            return ResponseEntity.ok("Service deleted successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Service not found");
    }
}
