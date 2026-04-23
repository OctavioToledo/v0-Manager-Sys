package com.demoV1Project.domain.repository;

import com.demoV1Project.domain.model.BusinessHoliday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusinessHolidayRepository extends JpaRepository<BusinessHoliday, Long> {
    List<BusinessHoliday> findByBusinessIdOrderByHolidayDateAsc(Long businessId);
}
