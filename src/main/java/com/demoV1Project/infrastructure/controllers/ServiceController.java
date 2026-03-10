package com.demoV1Project.infrastructure.controllers;

import com.demoV1Project.application.mapper.ServiceMapper;
import com.demoV1Project.application.service.BusinessService;
import com.demoV1Project.application.service.ServiceService;
import com.demoV1Project.domain.dto.ServiceDto.ServiceCreateDto;
import com.demoV1Project.domain.dto.ServiceDto.ServiceDto;
import com.demoV1Project.domain.dto.ServiceDto.ServiceShortDto;
import com.demoV1Project.domain.dto.ServiceDto.ServiceUpdateDto;
import com.demoV1Project.domain.model.Employee;
import com.demoV1Project.domain.model.Service;
import com.demoV1Project.application.service.EmployeeService;
import com.demoV1Project.shared.security.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/service")
@RequiredArgsConstructor
public class ServiceController {

    private final ServiceService serviceService;
    private final BusinessService businessService;
    private final ServiceMapper serviceMapper;
    private final TenantContext tenantContext;
    private final EmployeeService employeeService;

    @GetMapping("/findAll")
    public ResponseEntity<List<ServiceDto>> findByBusinessId(@RequestParam Long businessId) {
        tenantContext.validateBusinessOwnership(businessId);
        List<ServiceDto> services = serviceService.findByBusinessId(businessId);
        return ResponseEntity.ok(services);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<ServiceDto> findById(@PathVariable Long id) {
        return serviceService.findById(id)
                .map(service -> {
                    tenantContext.validateBusinessOwnership(service.getBusiness().getId());
                    return ResponseEntity.ok(serviceMapper.toDto(service));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody ServiceCreateDto serviceCreateDto) {
        if (serviceCreateDto.getBusinessId() == null) {
            return ResponseEntity.badRequest().body("Business ID is required");
        }
        tenantContext.validateBusinessOwnership(serviceCreateDto.getBusinessId());

        return businessService.findById(serviceCreateDto.getBusinessId())
                .map(business -> {
                    Service service = serviceMapper.toEntity(serviceCreateDto);
                    service.setBusiness(business);
                    Service savedService = serviceService.save(service);

                    if (serviceCreateDto.getEmployeeIds() != null && !serviceCreateDto.getEmployeeIds().isEmpty()) {
                        List<Employee> employees = employeeService.findAll().stream()
                                .filter(e -> serviceCreateDto.getEmployeeIds().contains(e.getId()))
                                .toList();
                        for (Employee emp : employees) {
                            if (!emp.getServices().contains(savedService)) {
                                emp.getServices().add(savedService);
                                employeeService.save(emp);
                            }
                        }
                    }

                    return ResponseEntity.status(HttpStatus.CREATED).body(savedService.getId());
                })
                .orElse((ResponseEntity.badRequest().build()));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody ServiceUpdateDto serviceUpdateDto) {
        Service service = serviceService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Service Not Found"));
        tenantContext.validateBusinessOwnership(service.getBusiness().getId());
        serviceMapper.updateEntity(serviceUpdateDto, service);
        Service savedService = serviceService.save(service);

        if (serviceUpdateDto.getEmployeeIds() != null) {
            List<Employee> currentEmployees = new ArrayList<>(service.getEmployees());
            for (Employee emp : currentEmployees) {
                if (!serviceUpdateDto.getEmployeeIds().contains(emp.getId())) {
                    emp.getServices().remove(service);
                    employeeService.save(emp);
                }
            }
            List<Employee> allEmployees = employeeService.findAll();
            List<Employee> newEmployees = allEmployees.stream()
                    .filter(e -> serviceUpdateDto.getEmployeeIds().contains(e.getId()))
                    .toList();
            for (Employee emp : newEmployees) {
                if (!emp.getServices().contains(service)) {
                    emp.getServices().add(service);
                    employeeService.save(emp);
                }
            }
        }

        return ResponseEntity.ok("Service Updated Successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        Service service = serviceService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Service Not Found"));
        tenantContext.validateBusinessOwnership(service.getBusiness().getId());

        List<Employee> currentEmployees = new ArrayList<>(service.getEmployees());
        for (Employee emp : currentEmployees) {
            emp.getServices().remove(service);
            employeeService.save(emp);
        }

        serviceService.deleteById(id);
        return ResponseEntity.ok("Service deleted successfully");
    }
}
