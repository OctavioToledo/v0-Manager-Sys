package com.demoV1Project.infrastructure.controllers;

import com.demoV1Project.domain.dto.EmployeeDto.EmployeeDto;

import com.demoV1Project.domain.model.Employee;
import com.demoV1Project.application.service.BusinessService;
import com.demoV1Project.application.service.EmployeeService;
import com.demoV1Project.application.mapper.EmployeeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v0/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final BusinessService businessService;
    private final EmployeeMapper employeeMapper;

    @GetMapping("/findAll")
    public ResponseEntity<List<EmployeeDto>> findAll() {
        List<Employee> employees = employeeService.findAll();
        List<EmployeeDto> employeeDtos = employeeMapper.toDtoList(employees);
        return ResponseEntity.ok(employeeDtos);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<EmployeeDto> findById(@PathVariable Long id) {
        return employeeService.findById(id)
                .map(employeeMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody EmployeeDto employeeDto) {
        if (employeeDto.getBusinessId() == null) {
            return ResponseEntity.badRequest().body("Business ID is required");
        }

        return businessService.findById(employeeDto.getBusinessId())
                .map(business -> {
                    Employee employee = employeeMapper.toEntity(employeeDto);
                    employee.setBusiness(business);
                    employeeService.save(employee);
                    return ResponseEntity.status(HttpStatus.CREATED).body("Employee saved successfully");
                })
                .orElse(ResponseEntity.badRequest().body("Business not found"));
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        if (employeeService.findById(id).isPresent()) {
            employeeService.deleteById(id);
            return ResponseEntity.ok("Employee deleted successfully");
        }
        return ResponseEntity.notFound().build();
    }
}