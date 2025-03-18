package com.demoV1Project.application.mapper;

import com.demoV1Project.domain.dto.AppointmentDto.AppointmentDto;
import com.demoV1Project.domain.model.Appointment;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AppointmentMapper {

    private final ModelMapper modelMapper;

    public AppointmentMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public AppointmentDto toDto(Appointment appointment) {
        return modelMapper.map(appointment, AppointmentDto.class);
    }

    public Appointment toEntity(AppointmentDto appointmentDto) {
        return modelMapper.map(appointmentDto, Appointment.class);
    }

    public List<AppointmentDto> toDtoList(List<Appointment> appointments) {
        return appointments.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

}
