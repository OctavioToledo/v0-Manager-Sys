package com.demoV1Project.domain.repository;

import com.demoV1Project.domain.model.Employee;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @EntityGraph(attributePaths = { "business" }) // Carga EAGER de business para evitar LazyInitializationException
    Optional<Employee> findWithBusinessById(Long employeeId);

    List<Employee> findByServicesId(Long serviceId);

    List<Employee> findByBusinessId(Long businessId);
}
