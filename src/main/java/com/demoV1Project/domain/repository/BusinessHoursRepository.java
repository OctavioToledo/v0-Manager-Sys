package com.demoV1Project.domain.repository;

import com.demoV1Project.domain.model.BusinessHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

@Repository
public interface BusinessHoursRepository extends JpaRepository<BusinessHours, Long> {
    List<BusinessHours> findByBusinessId(Long businessId);
    Optional<BusinessHours> findByBusinessIdAndDayOfWeek(Long businessId, DayOfWeek dayOfWeek);

    void deleteByBusinessId(Long businessId);
}
