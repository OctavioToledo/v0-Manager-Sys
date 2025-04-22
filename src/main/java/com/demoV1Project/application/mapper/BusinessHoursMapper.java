package com.demoV1Project.application.mapper;

import com.demoV1Project.domain.dto.BusinessHours.BusinessHoursDto;
import com.demoV1Project.domain.model.BusinessHours;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
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

        // Conversión segura de horarios matutinos
        if (dto.getOpeningMorningTime() != null) {
            entity.setOpeningMorningTime(LocalTime.parse(dto.getOpeningMorningTime()));
        }
        if (dto.getClosingMorningTime() != null) {
            entity.setClosingMorningTime(LocalTime.parse(dto.getClosingMorningTime()));
        }

        // Conversión segura de horarios vespertinos (opcionales)
        if (dto.getOpeningEveningTime() != null && !dto.getOpeningEveningTime().isEmpty()) {
            entity.setOpeningEveningTime(LocalTime.parse(dto.getOpeningEveningTime()));
        }
        if (dto.getClosingEveningTime() != null && !dto.getClosingEveningTime().isEmpty()) {
            entity.setClosingEveningTime(LocalTime.parse(dto.getClosingEveningTime()));
        }

        return entity;
    }

    public BusinessHoursDto toDto(BusinessHours entity) {
        if (entity == null) {
            return null;
        }

        return BusinessHoursDto.builder()
                .dayOfWeek(entity.getDayOfWeek())
                .openingMorningTime(entity.getOpeningMorningTime() != null ?
                        entity.getOpeningMorningTime().toString() : null)
                .closingMorningTime(entity.getClosingMorningTime() != null ?
                        entity.getClosingMorningTime().toString() : null)
                .openingEveningTime(entity.getOpeningEveningTime() != null ?
                        entity.getOpeningEveningTime().toString() : null)
                .closingEveningTime(entity.getClosingEveningTime() != null ?
                        entity.getClosingEveningTime().toString() : null)
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