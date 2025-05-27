package com.demoV1Project.application.service;

import com.demoV1Project.domain.dto.BusinessDto.BusinessDto;
import com.demoV1Project.domain.dto.BusinessDto.BusinessRegistrationDto;

public interface BusinessRegistrationService {
    BusinessDto registerBusiness(BusinessRegistrationDto registrationDto);
}
