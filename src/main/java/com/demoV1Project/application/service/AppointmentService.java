package com.demoV1Project.application.service;

import com.demoV1Project.domain.dto.AppointmentDto.AppointmentCreateDto;
import com.demoV1Project.domain.dto.AppointmentDto.AppointmentDto;
import com.demoV1Project.domain.dto.AppointmentGridDto.AppointmentGridDto;
import com.demoV1Project.domain.model.Appointment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AppointmentService {
    List<Appointment> findAll();

    Page<Appointment> findAll(Pageable pageable);

    Optional<Appointment> findById(Long id);

    void save(Appointment appointment);

    void deleteById(Long id);

    Appointment createAndSaveAppointment(AppointmentCreateDto appointmentCreateDto);

    // METODO DE CREACION PARA LA GRILLA
    AppointmentGridDto getAppointmentGrid(Long serviceId, LocalDateTime date);
}
