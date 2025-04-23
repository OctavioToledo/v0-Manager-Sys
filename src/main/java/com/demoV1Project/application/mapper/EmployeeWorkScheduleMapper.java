package com.demoV1Project.application.mapper;

import com.demoV1Project.domain.dto.EmployeeWorkScheduleDto.EmployeeWorkScheduleDto;
import com.demoV1Project.domain.dto.EmployeeWorkScheduleDto.EmployeeWorkScheduleRequest;
import com.demoV1Project.domain.model.EmployeeWorkSchedule;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
@Component
public class EmployeeWorkScheduleMapper {

    public EmployeeWorkSchedule toEntity(EmployeeWorkScheduleRequest dto) {
        if (dto == null) return null;

        EmployeeWorkSchedule entity = new EmployeeWorkSchedule();
        entity.setDayOfWeek(dto.getDayOfWeek());

        // Convertir horarios
        if (dto.getOpeningMorningTime() != null) {
            entity.setOpeningMorningTime(LocalTime.parse(dto.getOpeningMorningTime()));
        }
        if (dto.getClosingMorningTime() != null) {
            entity.setClosingMorningTime(LocalTime.parse(dto.getClosingMorningTime()));
        }

        if (dto.getOpeningEveningTime() != null && !dto.getOpeningEveningTime().isEmpty()) {
            entity.setOpeningEveningTime(LocalTime.parse(dto.getOpeningEveningTime()));
        }
        if (dto.getClosingEveningTime() != null && !dto.getClosingEveningTime().isEmpty()) {
            entity.setClosingEveningTime(LocalTime.parse(dto.getClosingEveningTime()));
        }

        return entity;
    }

    public EmployeeWorkScheduleDto toDto(EmployeeWorkSchedule entity) {
        if (entity == null) return null;

        return EmployeeWorkScheduleDto.builder()
                .id(entity.getId())
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

    public List<EmployeeWorkScheduleDto> toDtoList(List<EmployeeWorkSchedule> entities) {
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
