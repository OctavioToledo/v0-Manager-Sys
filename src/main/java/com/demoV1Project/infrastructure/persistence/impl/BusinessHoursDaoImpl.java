package com.demoV1Project.infrastructure.persistence.impl;

import com.demoV1Project.domain.model.BusinessHours;
import com.demoV1Project.domain.repository.BusinessHoursRepository;
import com.demoV1Project.infrastructure.persistence.BusinessHoursDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BusinessHoursDaoImpl implements BusinessHoursDao {
    private final BusinessHoursRepository businessHoursRepository;

    @Override
    public List<BusinessHours> saveAll(List<BusinessHours> businessHours) {
        return businessHoursRepository.saveAll(businessHours);
    }

    @Override
    public List<BusinessHours> findByBusinessId(Long businessId) {
        return businessHoursRepository.findByBusinessId(businessId);
    }

    @Override
    public void deleteByBusinessId(Long businessId) {
        businessHoursRepository.deleteByBusinessId(businessId);
    }
}
