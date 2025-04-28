package com.demoV1Project.domain.repository;

import com.demoV1Project.domain.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    List<Service> findByBusinessId(Long businessId);

}
