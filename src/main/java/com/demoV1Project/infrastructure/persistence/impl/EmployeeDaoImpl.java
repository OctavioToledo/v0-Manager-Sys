package com.demoV1Project.infrastructure.persistence.impl;

import com.demoV1Project.domain.model.Employee;
import com.demoV1Project.infrastructure.persistence.EmployeeDao;
import com.demoV1Project.domain.repository.EmployeeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class EmployeeDaoImpl implements EmployeeDao {

    @Autowired
    private final EmployeeRepository employeeRepository;
    @PersistenceContext
    private EntityManager entityManager;

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

    @Override
    public List<Employee> findByBusinessId(Long businessId) {
        String query = "SELECT e FROM Employee e WHERE e.business.id = :businessId";
        return entityManager.createQuery(query, Employee.class)
                .setParameter("businessId", businessId)
                .getResultList();
    }
}
