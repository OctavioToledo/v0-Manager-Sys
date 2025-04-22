package com.demoV1Project.application.mapper;

import com.demoV1Project.domain.dto.EmployeeWorkScheduleDto.EmployeeWorkScheduleDto;
import com.demoV1Project.domain.dto.EmployeeWorkScheduleDto.EmployeeWorkScheduleRequest;
import com.demoV1Project.domain.model.EmployeeWorkSchedule;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EmployeeWorkScheduleMapper {
    private final ModelMapper modelMapper;

    public EmployeeWorkScheduleMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public EmployeeWorkSchedule toEntity(EmployeeWorkScheduleRequest dto) {
        return modelMapper.map(dto, EmployeeWorkSchedule.class);
    }

    public EmployeeWorkScheduleDto toDto(EmployeeWorkSchedule entity) {
        return modelMapper.map(entity, EmployeeWorkScheduleDto.class);
    }

    public List<EmployeeWorkScheduleDto> toDtoList(List<EmployeeWorkSchedule> entities) {
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}