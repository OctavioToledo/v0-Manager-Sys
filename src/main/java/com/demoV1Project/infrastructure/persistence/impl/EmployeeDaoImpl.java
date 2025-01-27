package com.demoV1Project.infrastructure.persistence.impl;

import com.demoV1Project.domain.model.Employee;
import com.demoV1Project.infrastructure.persistence.EmployeeDao;
import com.demoV1Project.domain.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class EmployeeDaoImpl implements EmployeeDao {

    @Autowired
    private final EmployeeRepository employeeRepository;

    public EmployeeDaoImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }


    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> findById(Long id) {
        return employeeRepository.findById(id);
    }

    @Override
    public void save(Employee employee) {
        employeeRepository.save(employee);
    }

    @Override
    public void deleteById(Long id) {
        employeeRepository.deleteById(id);
    }
}
