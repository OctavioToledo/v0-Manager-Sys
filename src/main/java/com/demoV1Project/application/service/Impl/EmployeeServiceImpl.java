package com.demoV1Project.application.service.Impl;

import com.demoV1Project.application.mapper.EmployeeMapper;
import com.demoV1Project.domain.dto.EmployeeDto.EmployeeDetailDto;
import com.demoV1Project.domain.model.Employee;
import com.demoV1Project.infrastructure.persistence.EmployeeDao;
import com.demoV1Project.application.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private final EmployeeDao employeeDao;

    @Autowired
    private final EmployeeMapper employeeMapper;

    public EmployeeServiceImpl(EmployeeDao employeeDao, EmployeeMapper employeeMapper) {
        this.employeeDao = employeeDao;
        this.employeeMapper = employeeMapper;
    }


    @Override
    public List<Employee> findAll() {
        return employeeDao.findAll();
    }

    @Override
    public Optional<Employee> findById(Long id) {
        return employeeDao.findById(id);
    }

    @Override
    public Employee save(Employee employee) {
        employeeDao.save(employee);
        return employee;
    }

    @Override
    public void deleteById(Long id) {
        employeeDao.deleteById(id);
    }

    @Override
    public List<EmployeeDetailDto> findByBusinessId(Long businessId) {
        List<Employee> employees = employeeDao.findByBusinessId(businessId);
        return employeeMapper.toDetailListDto(employees);
    }
}
