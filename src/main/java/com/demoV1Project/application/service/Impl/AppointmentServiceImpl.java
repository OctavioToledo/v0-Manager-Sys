package com.demoV1Project.application.service.Impl;

import com.demoV1Project.application.exceptions.InvalidAppointmentGridException;
import com.demoV1Project.application.service.*;
import com.demoV1Project.domain.dto.AppointmentDto.AppointmentCreateDto;

import com.demoV1Project.domain.dto.AppointmentGridDto.AppointmentGridDto;
import com.demoV1Project.domain.dto.AppointmentGridDto.EmployeeScheduleGridDto;
import com.demoV1Project.domain.dto.AppointmentGridDto.TimeSlotDto;
import com.demoV1Project.domain.model.*;

import com.demoV1Project.domain.repository.*;
import com.demoV1Project.infrastructure.persistence.AppointmentDao;

import com.demoV1Project.util.enums.AppointmentStatus;
import com.demoV1Project.util.enums.Auxiliar.TimeSlotUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private final AppointmentDao appointmentDao;

    @Autowired
    private final AppointmentRepository appointmentRepository;


    @Autowired
    private final EmployeeService employeeService;

    @Autowired
    private final EmployeeRepository employeeRepository;

    @Autowired
    private final ServiceRepository serviceRepository;

    @Autowired
    private final ServiceService serviceService;

    @Autowired
    private final UserService userService;

    @Autowired
    private final BusinessService businessService;

    @Autowired
    private final BusinessHoursRepository businessHoursRepository;

    @Autowired
    private final EmployeeWorkScheduleRepository employeeWorkScheduleRepository;

    @Override
    public List<Appointment> findAll() {
        return appointmentDao.findAll();
    }

    @Override
    public Optional<Appointment> findById(Long id) {
        return appointmentDao.findById(id);
    }

    @Override
    public void save(Appointment appointment) {
        appointmentDao.save(appointment);
    }


    @Override
    public void deleteById(Long id) {
        appointmentDao.deleteById(id);
    }

    @Override
    public Appointment createAndSaveAppointment(AppointmentCreateDto appointmentCreateDto) {
        // Validar relaciones
        Employee employee = employeeService.findById(appointmentCreateDto.getEmployeeId())
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));

        Business business = businessService.findById(appointmentCreateDto.getBusinessId())
                .orElseThrow(()-> new IllegalArgumentException("Business not found"));

        com.demoV1Project.domain.model.Service service = serviceService.findById(appointmentCreateDto.getServiceId())
                .orElseThrow(() -> new IllegalArgumentException("Service not found"));

        User user = userService.findById(appointmentCreateDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Crear la entidad Appointment
        Appointment appointment = Appointment.builder()
                .date(appointmentCreateDto.getDate())
                .status(AppointmentStatus.valueOf(appointmentCreateDto.getStatus()))
                .business(business)
                .employee(employee)
                .service(service)
                .user(user)
                .build();

        // Guardar en la base de datos
        appointmentDao.save(appointment);

        return appointment;
    }

    // METODO DE CREACION PARA LA GRILLA
    @Override
    public AppointmentGridDto getAppointmentGrid(Long serviceId, LocalDateTime date) {
        // Obtener el servicio y su duración
        Service service = serviceService.findById(serviceId)
                .orElseThrow(() -> new InvalidAppointmentGridException("Service not found with id: " + serviceId));
        int duration = service.getDuration();

        // Obtener empleados que brindan ese servicio
        List<Employee> employees = employeeRepository.findByServicesId(serviceId);

        // Obtener el horario del negocio según el día de la semana
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        BusinessHours businessHours = businessHoursRepository
                .findByBusinessIdAndDayOfWeek(service.getBusiness().getId(), dayOfWeek)
                .orElseThrow(() -> new InvalidAppointmentGridException("No se encontraron horarios disponibles"));

        if (businessHours == null) {
            throw new InvalidAppointmentGridException("Business hours not found for day: " + dayOfWeek);
        }

        // Generar los time slots según el horario del negocio
        List<TimeSlotDto> timeSlots = TimeSlotUtils.generateTimeSlots(businessHours, duration);

        // Para cada empleado, obtener su disponibilidad en esos time slots
        List<EmployeeScheduleGridDto> employeeScheduleGridDtos = new ArrayList<>();

        for (Employee employee : employees) {
            // Obtener el horario del empleado para ese día
            EmployeeWorkSchedule schedule = employeeWorkScheduleRepository
                    .findByEmployeeIdAndDayOfWeek(employee.getId(), dayOfWeek);

            // Obtener turnos reservados de ese empleado ese día
            List<Appointment> takenAppointments = appointmentRepository
                    .findByEmployeeIdAndDate(employee.getId(), date);

            List<Boolean> availability = TimeSlotUtils.calculateAvailability(
                    timeSlots, schedule, takenAppointments, duration
            );

            List<TimeSlotDto> timeSlotDtos = new ArrayList<>();
            for (int i = 0; i < timeSlots.size(); i++) {
                timeSlotDtos.add(new TimeSlotDto(timeSlots.get(i), availability.get(i)));
            }

            employeeScheduleGridDtos.add(
                    new EmployeeScheduleGridDto(employee.getId(), employee.getName(), timeSlotDtos)
            );

        }


        return AppointmentGridDto.builder()
                .serviceName(service.getName())
                .serviceDuration(duration)
                .timeSlots(timeSlots)
                .employeeAvailabilities(employeeScheduleGridDtos)
                .build();
    }

}
