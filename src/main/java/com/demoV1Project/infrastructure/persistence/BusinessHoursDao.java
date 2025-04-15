package com.demoV1Project.infrastructure.persistence;

import com.demoV1Project.domain.model.BusinessHours;

import java.util.List;

public interface BusinessHoursDao {
    List<BusinessHours> saveAll(List<BusinessHours> businessHours);
    List<BusinessHours> findByBusinessId(Long businessId);
    void deleteByBusinessId(Long businessId);
}
