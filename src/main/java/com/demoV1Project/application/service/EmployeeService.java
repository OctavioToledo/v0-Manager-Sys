package com.demoV1Project.application.service;

import com.demoV1Project.domain.dto.EmployeeDto.EmployeeDetailDto;
import com.demoV1Project.domain.dto.EmployeeDto.EmployeeDto;
import com.demoV1Project.domain.dto.EmployeeDto.EmployeeShortDto;
import com.demoV1Project.domain.model.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    List<Employee> findAll();
    Optional<Employee> findById(Long id);
    void save(Employee employee);
    void deleteById(Long id);
    List<EmployeeDetailDto> findByBusinessId(Long businessId);

}
