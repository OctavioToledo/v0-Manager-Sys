package com.demoV1Project.application.service;
import com.demoV1Project.domain.dto.BusinessHours.BusinessHoursDto;
import java.util.List;

public interface BusinessHoursService {
    List<BusinessHoursDto> saveHoursForBusiness(Long businessId, List<BusinessHoursDto> requests);
    List<BusinessHoursDto> findByBusinessId(Long businessId);
    void deleteByBusinessId(Long businessId);
}
