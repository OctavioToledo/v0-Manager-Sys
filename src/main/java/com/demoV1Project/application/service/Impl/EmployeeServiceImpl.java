package com.demoV1Project.application.service.Impl;

import com.demoV1Project.application.mapper.EmployeeMapper;
import com.demoV1Project.domain.dto.EmployeeDto.EmployeeDetailDto;
import com.demoV1Project.domain.model.Employee;
import com.demoV1Project.domain.repository.EmployeeRepository;
import com.demoV1Project.application.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> findById(Long id) {
        return employeeRepository.findById(id);
    }

    @Override
    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public void deleteById(Long id) {
        employeeRepository.deleteById(id);
    }

    @Override
    public List<EmployeeDetailDto> findByBusinessId(Long businessId) {
        List<Employee> employees = employeeRepository.findByBusinessId(businessId);
        return employeeMapper.toDetailListDto(employees);
    }
}
