package com.demoV1Project.infrastructure.controllers;

import com.demoV1Project.application.service.ServiceService;
import com.demoV1Project.domain.dto.EmployeeDto.EmployeeCreateDto;
import com.demoV1Project.domain.dto.EmployeeDto.EmployeeDetailDto;
import com.demoV1Project.domain.dto.EmployeeDto.EmployeeDto;

import com.demoV1Project.domain.dto.EmployeeDto.EmployeeUpdateDto;
import com.demoV1Project.domain.model.Employee;
import com.demoV1Project.application.service.BusinessService;
import com.demoV1Project.application.service.EmployeeService;
import com.demoV1Project.application.mapper.EmployeeMapper;
import com.demoV1Project.domain.model.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/v0/employee")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final BusinessService businessService;
    private final EmployeeMapper employeeMapper;
    private final ServiceService serviceService;

    @GetMapping("/findAll/")
    public ResponseEntity<List<EmployeeDetailDto>> findByBusinessId(@RequestParam Long businessId) {
        List<EmployeeDetailDto> employeeDtos = employeeService.findByBusinessId(businessId);
        return ResponseEntity.ok(employeeDtos);
    }



    @GetMapping("/find/{id}")
    public ResponseEntity<EmployeeDto> findById(@PathVariable Long id) {
        return employeeService.findById(id)
                .map(employeeMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<EmployeeDetailDto> employeeDetail(@PathVariable Long id){
        return employeeService.findById(id)
                .map(employeeMapper::toDetailDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody EmployeeCreateDto employeeCreateDto) {
        if (employeeCreateDto.getBusinessId() == null) {
            return ResponseEntity.badRequest().body("Business ID is required");
        }

        return businessService.findById(employeeCreateDto.getBusinessId())
                .map(business -> {
                    Employee employee = employeeMapper.toEntity(employeeCreateDto);
                    employee.setBusiness(business);

                    if (employeeCreateDto.getServiceIds() != null && !employeeCreateDto.getServiceIds().isEmpty()) {
                        List<Service> services = serviceService.findAllById(employeeCreateDto.getServiceIds());
                        employee.setServices(services);
                    }

                    Employee savedEmployee = employeeService.save(employee);
                    return ResponseEntity.status(HttpStatus.CREATED).body(savedEmployee.getId());
                })
                .orElse((ResponseEntity.badRequest().build()));
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody EmployeeUpdateDto employeeUpdateDto){
        Employee employee = employeeService.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Employee Not Found"));
        employeeMapper.updateEntity(employeeUpdateDto, employee);
        employeeService.save(employee);
        return ResponseEntity.ok("Employee Updated Successfully");
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