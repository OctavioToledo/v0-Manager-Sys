package com.demoV1Project.infrastructure.controllers;


import com.demoV1Project.domain.dto.EmployeeDto;
import com.demoV1Project.domain.model.Business;
import com.demoV1Project.domain.model.Employee;
import com.demoV1Project.domain.model.Service;
import com.demoV1Project.application.service.BusinessService;
import com.demoV1Project.application.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v0/employee")
@RequiredArgsConstructor
public class EmployeeController {

    @Autowired
    private final EmployeeService employeeService;

    @Autowired
    private final BusinessService businessService;


    @GetMapping("/findAll")
    public ResponseEntity<List<EmployeeDto>> findAll() {
        List<EmployeeDto> employees = employeeService.findAll()
                .stream()
                .map(employee -> EmployeeDto.builder()
                        .id(employee.getId())
                        .name(employee.getName())
                        .workSchedule(employee.getWorkSchedule())
                        .profilePicture(employee.getProfilePicture())
                        .businessId(employee.getBusiness().getId())
                        .serviceIds(employee.getServices()
                                .stream()
                                .map(Service::getId)
                                .toList())
                        .build())
                .toList();
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<Employee> employeeOptional = employeeService.findById(id);
        if (employeeOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found");
        }

        Employee employee = employeeOptional.get();
        EmployeeDto employeeDto = EmployeeDto.builder()
                .id(employee.getId())
                .name(employee.getName())
                .workSchedule(employee.getWorkSchedule())
                .profilePicture(employee.getProfilePicture())
                .businessId(employee.getBusiness().getId())
                .serviceIds(employee.getServices()
                        .stream()
                        .map(Service::getId)
                        .toList())
                .build();
        return ResponseEntity.ok(employeeDto);
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody EmployeeDto employeeDto) {
        // Verificar que el businessId esté presente en el DTO
        if (employeeDto.getBusinessId() == null) {
            return ResponseEntity.badRequest().body("Business ID is required");
        }

        // Buscar el negocio correspondiente
        Optional<Business> businessOptional = businessService.findById(employeeDto.getBusinessId());
        if (businessOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Business not found");
        }

        Business business = businessOptional.get();

        // Crear y asociar el Employee
        Employee employee = Employee.builder()
                .name(employeeDto.getName())
                .workSchedule(employeeDto.getWorkSchedule())
                .profilePicture(employeeDto.getProfilePicture())
                .business(business) // Asociar el negocio aquí
                .build();

        employeeService.save(employee);

        return ResponseEntity.status(HttpStatus.CREATED).body("Employee saved successfully");
    }


    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        employeeService.deleteById(id);
        return ResponseEntity.ok("Employee deleted successfully");
    }
}

