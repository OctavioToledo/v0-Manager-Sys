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
        entity.setIsWorkingDay(dto.getIsWorkingDay());

        entity.setMorningStart(dto.getMorningStart());
        entity.setMorningEnd(dto.getMorningEnd());
        entity.setAfternoonStart(dto.getAfternoonStart());
        entity.setAfternoonEnd(dto.getAfternoonEnd());

        return entity;
    }

    public EmployeeWorkScheduleDto toDto(EmployeeWorkSchedule entity) {
        if (entity == null)
            return null;

        String[] days = {"", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};
        String dayName = (entity.getDayOfWeek() != null && entity.getDayOfWeek() >= 1 && entity.getDayOfWeek() <= 7) 
            ? days[entity.getDayOfWeek()] : "";

        return EmployeeWorkScheduleDto.builder()
                .id(entity.getId())
                .dayOfWeek(entity.getDayOfWeek())
                .dayOfWeekName(dayName)
                .isWorkingDay(entity.getIsWorkingDay())
                .morningStart(entity.getMorningStart())
                .morningEnd(entity.getMorningEnd())
                .afternoonStart(entity.getAfternoonStart())
                .afternoonEnd(entity.getAfternoonEnd())
                .build();
    }

    public List<EmployeeWorkScheduleDto> toDtoList(List<EmployeeWorkSchedule> entities) {
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
