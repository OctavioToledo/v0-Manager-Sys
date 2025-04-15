package com.demoV1Project.domain.repository;

import com.demoV1Project.domain.model.BusinessHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusinessHoursRepository extends JpaRepository<BusinessHours, Long> {
    List<BusinessHours> findByBusinessId(Long businessId);

    void deleteByBusinessId(Long businessId);
}
