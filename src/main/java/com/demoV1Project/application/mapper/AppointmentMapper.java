package com.demoV1Project.application.mapper;

import com.demoV1Project.domain.dto.AppointmentDto;
import com.demoV1Project.domain.model.Appointment;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {

    @Autowired
    private ModelMapper modelMapper;

    public AppointmentDto toDto(Appointment appointment){
        return modelMapper.map(appointment, AppointmentDto.class);
    }

    public Appointment toEntity(AppointmentDto appointmentDto){
        return  modelMapper.map(appointmentDto, Appointment.class);
    }

}
