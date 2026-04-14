package com.demoV1Project.application.service;

import com.demoV1Project.domain.dto.AppointmentDto.AppointmentCreateDto;
import com.demoV1Project.domain.dto.AppointmentGridDto.AppointmentGridDto;
import com.demoV1Project.domain.model.Appointment;

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

    Appointment createPendingAppointment(AppointmentCreateDto appointmentCreateDto);

    boolean checkCollision(Long employeeId, java.time.LocalDate date, String startTime, String endTime);

    boolean checkCollisionWithApproved(Long employeeId, java.time.LocalDate date, String startTime, String endTime);

    // METODO DE CREACION PARA LA GRILLA
    AppointmentGridDto getAppointmentGrid(Long serviceId, java.time.LocalDate date);

    List<Appointment> findByBusinessIdAndDate(Long businessId, java.time.LocalDate date);

    List<Appointment> findByBusinessIdAndDateRange(Long businessId, java.time.LocalDate startDate, java.time.LocalDate endDate);

    List<Appointment> findByEmployeeIdAndDate(Long employeeId, java.time.LocalDate date);
}
