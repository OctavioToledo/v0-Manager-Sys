package com.demoV1Project.application.mapper;

import com.demoV1Project.domain.dto.BusinessDto;
import com.demoV1Project.domain.model.Business;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BusinessMapper {

    @Autowired
    private ModelMapper modelMapper;

    // Convertir entidad a DTO
    public BusinessDto toDto(Business business) {
        return modelMapper.map(business, BusinessDto.class);
    }

    // Convertir DTO a entidad
    public Business toEntity(BusinessDto businessDto) {
        return modelMapper.map(businessDto, Business.class);
    }

    // Convertir lista de entidades a lista de DTOs
    public List<BusinessDto> toDtoList(List<Business> businesses) {
        return businesses.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

}
