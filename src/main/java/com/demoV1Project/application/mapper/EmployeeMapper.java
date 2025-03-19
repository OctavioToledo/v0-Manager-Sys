package com.demoV1Project.application.mapper;

import com.demoV1Project.domain.dto.BusinessDto.BusinessUpdateDto;
import com.demoV1Project.domain.dto.EmployeeDto.*;
import com.demoV1Project.domain.model.Business;
import com.demoV1Project.domain.model.Employee;
import com.demoV1Project.domain.model.Service;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EmployeeMapper {

    private final ModelMapper modelMapper;

    public EmployeeMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public EmployeeDto toDto(Employee employee) {
        EmployeeDto employeeDto = modelMapper.map(employee, EmployeeDto.class);
        employeeDto.setBusinessId(employee.getBusiness().getId());
        employeeDto.setServiceIds(employee.getServices()
                .stream()
                .map(Service::getId)
                .collect(Collectors.toList()));
        return employeeDto;
    }

    public EmployeeDetailDto toDetailDto(Employee employee){
        return modelMapper.map(employee, EmployeeDetailDto.class);
    }

    public Employee toEntity(EmployeeCreateDto employeeCreateDto) {
        return modelMapper.map(employeeCreateDto, Employee.class);
    }

    public List<EmployeeDto> toDtoList(List<Employee> employees) {
        return employees.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public void updateEntity(EmployeeUpdateDto employeeUpdateDto, Employee employee){
        modelMapper.map(employeeUpdateDto, employee);
    }
}
