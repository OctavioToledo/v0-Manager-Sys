package com.demoV1Project.application.service.Impl;

import com.demoV1Project.application.exceptions.InvalidAppointmentGridException;
import com.demoV1Project.application.service.*;
import com.demoV1Project.domain.dto.AppointmentDto.AppointmentCreateDto;

import com.demoV1Project.domain.dto.AppointmentGridDto.AppointmentGridDto;
import com.demoV1Project.domain.dto.AppointmentGridDto.EmployeeScheduleGridDto;
import com.demoV1Project.domain.dto.AppointmentGridDto.TimeSlotDto;
import com.demoV1Project.domain.model.*;

import com.demoV1Project.domain.repository.*;

import com.demoV1Project.util.enums.AppointmentStatus;
import com.demoV1Project.util.enums.Auxiliar.TimeSlotUtils;
import lombok.RequiredArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

        private final AppointmentRepository appointmentRepository;
        private final EmployeeService employeeService;
        private final EmployeeRepository employeeRepository;
        private final ServiceRepository serviceRepository;
        private final ServiceService serviceService;
        private final UserService userService;
        private final BusinessService businessService;
        private final BusinessHoursRepository businessHoursRepository;
        private final EmployeeWorkScheduleRepository employeeWorkScheduleRepository;

        @Override
        public List<Appointment> findAll() {
                return appointmentRepository.findAll();
        }

        @Override
        public org.springframework.data.domain.Page<Appointment> findAll(
                        org.springframework.data.domain.Pageable pageable) {
                return appointmentRepository.findAll(pageable);
        }

        @Override
        public Optional<Appointment> findById(Long id) {
                return appointmentRepository.findById(id);
        }

        @Override
        public void save(Appointment appointment) {
                appointmentRepository.save(appointment);
        }

        @Override
        public void deleteById(Long id) {
                appointmentRepository.deleteById(id);
        }

        @Override
        public Appointment createAndSaveAppointment(AppointmentCreateDto appointmentCreateDto) {
                Employee employee = employeeService.findById(appointmentCreateDto.getEmployeeId())
                                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));

                Business business = businessService.findById(appointmentCreateDto.getBusinessId())
                                .orElseThrow(() -> new IllegalArgumentException("Business not found"));

                com.demoV1Project.domain.model.Service service = serviceService
                                .findById(appointmentCreateDto.getServiceId())
                                .orElseThrow(() -> new IllegalArgumentException("Service not found"));

                User user = userService.findById(appointmentCreateDto.getUserId())
                                .orElseThrow(() -> new IllegalArgumentException("User not found"));

                // === VALIDACIÓN 1: Employee pertenece al Business ===
                if (!employee.getBusiness().getId().equals(business.getId())) {
                        throw new IllegalArgumentException(
                                        "El empleado no pertenece al negocio seleccionado");
                }

                // === VALIDACIÓN 2: Service pertenece al Business ===
                if (!service.getBusiness().getId().equals(business.getId())) {
                        throw new IllegalArgumentException(
                                        "El servicio no pertenece al negocio seleccionado");
                }

                // === VALIDACIÓN 3: Sin solapamiento de citas ===
                LocalDateTime appointmentStart = appointmentCreateDto.getDate();
                LocalDateTime appointmentEnd = appointmentStart.plusMinutes(service.getDuration());

                List<Appointment> overlapping = appointmentRepository.findOverlappingAppointments(
                                employee.getId(), appointmentStart, appointmentEnd);

                if (!overlapping.isEmpty()) {
                        throw new IllegalArgumentException(
                                        "El empleado ya tiene una cita en ese horario");
                }

                // === VALIDACIÓN 4: Dentro del horario del negocio ===
                DayOfWeek dayOfWeek = appointmentStart.getDayOfWeek();
                Optional<BusinessHours> businessHoursOpt = businessHoursRepository
                                .findByBusinessIdAndDayOfWeek(business.getId(), dayOfWeek);

                if (businessHoursOpt.isEmpty()) {
                        throw new IllegalArgumentException(
                                        "El negocio no opera el día seleccionado");
                }

                BusinessHours bh = businessHoursOpt.get();
                java.time.LocalTime startTime = appointmentStart.toLocalTime();
                java.time.LocalTime endTime = appointmentEnd.toLocalTime();

                boolean inMorning = bh.getOpeningMorningTime() != null
                                && bh.getClosingMorningTime() != null
                                && !startTime.isBefore(bh.getOpeningMorningTime())
                                && !endTime.isAfter(bh.getClosingMorningTime());

                boolean inEvening = bh.getOpeningEveningTime() != null
                                && bh.getClosingEveningTime() != null
                                && !startTime.isBefore(bh.getOpeningEveningTime())
                                && !endTime.isAfter(bh.getClosingEveningTime());

                if (!inMorning && !inEvening) {
                        throw new IllegalArgumentException(
                                        "La cita está fuera del horario de atención del negocio");
                }

                Appointment appointment = Appointment.builder()
                                .date(appointmentCreateDto.getDate())
                                .status(AppointmentStatus.valueOf(appointmentCreateDto.getStatus()))
                                .business(business)
                                .employee(employee)
                                .service(service)
                                .user(user)
                                .build();

                appointmentRepository.save(appointment);

                return appointment;
        }

        // METODO DE CREACION PARA LA GRILLA
        @Override
        public AppointmentGridDto getAppointmentGrid(Long serviceId, LocalDateTime date) {
                Service service = serviceService.findById(serviceId)
                                .orElseThrow(() -> new InvalidAppointmentGridException(
                                                "Service not found with id: " + serviceId));
                int duration = service.getDuration();

                List<Employee> employees = employeeRepository.findByServicesId(serviceId);

                DayOfWeek dayOfWeek = date.getDayOfWeek();
                BusinessHours businessHours = businessHoursRepository
                                .findByBusinessIdAndDayOfWeek(service.getBusiness().getId(), dayOfWeek)
                                .orElseThrow(() -> new InvalidAppointmentGridException(
                                                "No se encontraron horarios disponibles"));

                List<TimeSlotDto> timeSlots = TimeSlotUtils.generateTimeSlots(businessHours, duration);

                List<EmployeeScheduleGridDto> employeeScheduleGridDtos = new ArrayList<>();

                for (Employee employee : employees) {
                        EmployeeWorkSchedule schedule = employeeWorkScheduleRepository
                                        .findByEmployeeIdAndDayOfWeek(employee.getId(), dayOfWeek);

                        List<Appointment> takenAppointments = appointmentRepository
                                        .findByEmployeeIdAndDate(employee.getId(), date);

                        List<Boolean> availability = TimeSlotUtils.calculateAvailability(
                                        timeSlots, schedule, takenAppointments, duration);

                        List<TimeSlotDto> timeSlotDtos = new ArrayList<>();
                        for (int i = 0; i < timeSlots.size(); i++) {
                                timeSlotDtos.add(new TimeSlotDto(timeSlots.get(i), availability.get(i)));
                        }

                        employeeScheduleGridDtos.add(
                                        new EmployeeScheduleGridDto(employee.getId(), employee.getName(),
                                                        timeSlotDtos));
                }

                return AppointmentGridDto.builder()
                                .serviceName(service.getName())
                                .serviceDuration(duration)
                                .timeSlots(timeSlots)
                                .employeeAvailabilities(employeeScheduleGridDtos)
                                .build();
        }

}
