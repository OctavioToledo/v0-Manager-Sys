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
        entity.setIsWorkingDay(dto.getIsWorkingDay());
        entity.setMorningStart(dto.getMorningStart());
        entity.setMorningEnd(dto.getMorningEnd());
        entity.setAfternoonStart(dto.getAfternoonStart());
        entity.setAfternoonEnd(dto.getAfternoonEnd());
        return entity;
    }

    public BusinessHoursDto toDto(BusinessHours entity) {
        if (entity == null) {
            return null;
        }
        return BusinessHoursDto.builder()
                .dayOfWeek(entity.getDayOfWeek())
                .isWorkingDay(entity.getIsWorkingDay())
                .morningStart(entity.getMorningStart())
                .morningEnd(entity.getMorningEnd())
                .afternoonStart(entity.getAfternoonStart())
                .afternoonEnd(entity.getAfternoonEnd())
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