package com.demoV1Project.infrastructure.persistence;


import com.demoV1Project.domain.model.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeDao {

    List<Employee> findAll();
    Optional<Employee> findById(Long id);
    void save(Employee employee);
    void deleteById(Long id);
}
