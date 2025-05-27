package com.demoV1Project.application.service.Impl;

import com.demoV1Project.application.mapper.AddressMapper;
import com.demoV1Project.application.mapper.BusinessHoursMapper;
import com.demoV1Project.application.mapper.BusinessMapper;
import com.demoV1Project.application.service.*;
import com.demoV1Project.domain.dto.BusinessDto.BusinessDto;
import com.demoV1Project.domain.dto.BusinessDto.BusinessRegistrationDto;
import com.demoV1Project.domain.dto.BusinessHours.BusinessHoursDto;
import com.demoV1Project.domain.model.Address;
import com.demoV1Project.domain.model.Business;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BusinessRegistrationServiceImpl implements BusinessRegistrationService {

    private final AddressService addressService;
    private final AddressMapper addressMapper;

    private final BusinessService businessService;
    private final BusinessMapper businessMapper;

    private final CategoryService categoryService;
    private final UserService userService;

    private final BusinessHoursService businessHoursService;
    private final BusinessHoursMapper businessHoursMapper;

    @Override
    @Transactional
    public BusinessDto registerBusiness(BusinessRegistrationDto dto) {
        // Crear Address
        Address address = addressMapper.toEntity(dto.getAddress());
        Address savedAddress = addressService.save(address);

        // Crear Business
        Business business = businessMapper.toEntity(dto.getBusiness());
        business.setAddress(savedAddress);
        business.setCategory(categoryService.findByIdOrThrow(dto.getCategoryId()));
        business.setUser(userService.findByIdOrThrow(dto.getUserId()));

        Business savedBusiness = businessService.save(business);

        // Crear BusinessHours
        List<BusinessHoursDto> businessHoursDtos = dto.getBusinessHours();
        businessHoursService.saveHoursForBusiness(savedBusiness.getId(), businessHoursDtos);

        // Retornar DTO del negocio creado
        return businessMapper.toDto(savedBusiness);
    }
}
