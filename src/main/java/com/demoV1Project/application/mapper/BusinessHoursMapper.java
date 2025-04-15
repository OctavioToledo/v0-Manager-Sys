package com.demoV1Project.application.mapper;

import com.demoV1Project.domain.dto.BusinessHours.BusinessHoursDto;
import com.demoV1Project.domain.model.BusinessHours;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BusinessHoursMapper {
    private final ModelMapper modelMapper;

    public BusinessHoursMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public BusinessHoursDto toDto(BusinessHours businessHours) {
        return modelMapper.map(businessHours, BusinessHoursDto.class);
    }

    public BusinessHours toEntity(BusinessHoursDto businessHoursDto) {
        return modelMapper.map(businessHoursDto, BusinessHours.class);
    }

    public List<BusinessHoursDto> toDtoList(List<BusinessHours> businessHoursList) {
        return businessHoursList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<BusinessHours> toEntityList(List<BusinessHoursDto> businessHoursDtoList) {
        return businessHoursDtoList.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}