package com.demoV1Project.application.mapper;

import com.demoV1Project.domain.dto.AppointmentDto.AppointmentCreateDto;
import com.demoV1Project.domain.dto.AppointmentDto.AppointmentDto;
import com.demoV1Project.domain.dto.AppointmentDto.AppointmentUpdateDto;
import com.demoV1Project.domain.model.Address;
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
        System.out.println("Business en Appointment: " + appointment.getBusiness()); // Debug
        AppointmentDto dto = modelMapper.map(appointment, AppointmentDto.class);
        if (appointment.getBusiness() != null) {
            dto.setBusinessId(appointment.getBusiness().getId());
            System.out.println("Business ID seteado: " + dto.getBusinessId()); // Debug
        }
        return dto;
    }


    public Appointment toEntity(AppointmentCreateDto appointmentCreateDto) {
        return modelMapper.map(appointmentCreateDto, Appointment.class);
    }

    public List<AppointmentDto> toDtoList(List<Appointment> appointments) {
        return appointments.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    public void updateEntity(AppointmentUpdateDto appointmentUpdateDto, Appointment appointment) {
        modelMapper.map(appointmentUpdateDto, appointment);
    }

}
