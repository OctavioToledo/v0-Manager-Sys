package com.demoV1Project.application.service;


import com.demoV1Project.domain.dto.AppointmentDto.AppointmentCreateDto;
import com.demoV1Project.domain.dto.AppointmentDto.AppointmentDto;
import com.demoV1Project.domain.dto.AppointmentGridDto.AppointmentGridDto;
import com.demoV1Project.domain.model.Appointment;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AppointmentService {
    List<Appointment> findAll();
    Optional<Appointment> findById(Long id);
    void save(Appointment appointment);
    void deleteById(Long id);
    Appointment createAndSaveAppointment(AppointmentCreateDto appointmentCreateDto);

    // METODO DE CREACION PARA LA GRILLA
    AppointmentGridDto getAppointmentGrid(Long serviceId, LocalDate date);
}
