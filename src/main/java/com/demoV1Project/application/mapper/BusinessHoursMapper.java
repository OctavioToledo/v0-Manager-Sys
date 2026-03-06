package com.demoV1Project.application.mapper;

import com.demoV1Project.domain.dto.BusinessHours.BusinessHoursDto;
import com.demoV1Project.domain.model.BusinessHours;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BusinessHoursMapper {

    public BusinessHours toEntity(BusinessHoursDto dto) {
        if (dto == null) {
            return null;
        }
        BusinessHours entity = new BusinessHours();
        entity.setDayOfWeek(dto.getDayOfWeek());
        entity.setOpeningMorningTime(dto.getOpeningMorningTime());
        entity.setClosingMorningTime(dto.getClosingMorningTime());
        entity.setOpeningEveningTime(dto.getOpeningEveningTime());
        entity.setClosingEveningTime(dto.getClosingEveningTime());
        return entity;
    }

    public BusinessHoursDto toDto(BusinessHours entity) {
        if (entity == null) {
            return null;
        }
        return BusinessHoursDto.builder()
                .dayOfWeek(entity.getDayOfWeek())
                .openingMorningTime(entity.getOpeningMorningTime())
                .closingMorningTime(entity.getClosingMorningTime())
                .openingEveningTime(entity.getOpeningEveningTime())
                .closingEveningTime(entity.getClosingEveningTime())
                .build();
    }

    public List<BusinessHours> toEntityList(List<BusinessHoursDto> dtoList) {
        return dtoList.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    public List<BusinessHoursDto> toDtoList(List<BusinessHours> entityList) {
        return entityList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}