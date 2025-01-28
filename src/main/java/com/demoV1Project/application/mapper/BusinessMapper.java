package com.demoV1Project.application.mapper;

import com.demoV1Project.domain.dto.BusinessDto;
import com.demoV1Project.domain.model.Business;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BusinessMapper {

    @Autowired
    private ModelMapper modelMapper;

    public BusinessDto toDto(Business business) {
        return modelMapper.map(business, BusinessDto.class);
    }

    public Business toEntity(BusinessDto businessDto) {
        return modelMapper.map(businessDto, Business.class);
    }

}
