package com.demoV1Project.application.mapper;

import com.demoV1Project.domain.dto.EmployeeWorkScheduleDto.EmployeeWorkScheduleDto;
import com.demoV1Project.domain.dto.EmployeeWorkScheduleDto.EmployeeWorkScheduleRequest;
import com.demoV1Project.domain.model.EmployeeWorkSchedule;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EmployeeWorkScheduleMapper {

    public EmployeeWorkSchedule toEntity(EmployeeWorkScheduleRequest dto) {
        if (dto == null)
            return null;

        EmployeeWorkSchedule entity = new EmployeeWorkSchedule();
        entity.setDayOfWeek(dto.getDayOfWeek());

        entity.setOpeningMorningTime(dto.getOpeningMorningTime());
        entity.setClosingMorningTime(dto.getClosingMorningTime());
        entity.setOpeningEveningTime(dto.getOpeningEveningTime());
        entity.setClosingEveningTime(dto.getClosingEveningTime());

        return entity;
    }

    public EmployeeWorkScheduleDto toDto(EmployeeWorkSchedule entity) {
        if (entity == null)
            return null;

        return EmployeeWorkScheduleDto.builder()
                .id(entity.getId())
                .dayOfWeek(entity.getDayOfWeek())
                .openingMorningTime(entity.getOpeningMorningTime())
                .closingMorningTime(entity.getClosingMorningTime())
                .openingEveningTime(entity.getOpeningEveningTime())
                .closingEveningTime(entity.getClosingEveningTime())
                .build();
    }

    public List<EmployeeWorkScheduleDto> toDtoList(List<EmployeeWorkSchedule> entities) {
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
