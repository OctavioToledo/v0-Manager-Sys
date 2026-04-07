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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

        private final AppointmentRepository appointmentRepository;
        private final EmployeeService employeeService;
        private final EmployeeRepository employeeRepository;
        private final ServiceService serviceService;
        private final UserService userService;
        private final BusinessService businessService;
        private final BusinessHoursRepository businessHoursRepository;
        private final EmployeeWorkScheduleRepository employeeWorkScheduleRepository;
        private final AppointmentSlotGenerator appointmentSlotGenerator;

        private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

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
        public boolean checkCollision(Long employeeId, LocalDate date, String startTime, String endTime) {
                return !appointmentRepository.findOverlappingAppointments(employeeId, date, startTime, endTime)
                                .isEmpty();
        }

        @Override
        public boolean checkCollisionWithApproved(Long employeeId, LocalDate date, String startTime, String endTime) {
                return appointmentRepository.countOverlappingApprovedAppointments(employeeId, date, startTime,
                                endTime) > 0;
        }

        @Override
        public Appointment createPendingAppointment(AppointmentCreateDto dto) {
                Employee employee = employeeService.findById(dto.getEmployeeId())
                                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));

                Business business = businessService.findById(dto.getBusinessId())
                                .orElseThrow(() -> new IllegalArgumentException("Business not found"));

                com.demoV1Project.domain.model.Service service = serviceService
                                .findById(dto.getServiceId())
                                .orElseThrow(() -> new IllegalArgumentException("Service not found"));

        User user = null;
        if (dto.getUserId() != null) {
            user = userService.findById(dto.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
        }

        if (!employee.getBusiness().getId().equals(business.getId())) {
            throw new IllegalArgumentException("El empleado no pertenece al negocio");
        }
        if (!service.getBusiness().getId().equals(business.getId())) {
            throw new IllegalArgumentException("El servicio no pertenece al negocio");
        }

        LocalTime start = LocalTime.parse(dto.getStartTime(), TIME_FORMATTER);
        String calculatedEndTime = start.plusMinutes(service.getDuration()).format(TIME_FORMATTER);

        Appointment appointment = Appointment.builder()
                .appointmentDate(dto.getAppointmentDate())
                .startTime(dto.getStartTime())
                .endTime(calculatedEndTime)
                .status(AppointmentStatus.PENDING)
                .business(business)
                .employee(employee)
                .service(service)
                .user(user)
                .clientName(dto.getClientName())
                .clientEmail(dto.getClientEmail())
                .clientPhone(dto.getClientPhone())
                .build();

        return appointmentRepository.save(appointment);
        }

        @Override
        public AppointmentGridDto getAppointmentGrid(Long serviceId, LocalDate date) {
                com.demoV1Project.domain.model.Service service = serviceService.findById(serviceId)
                                .orElseThrow(() -> new InvalidAppointmentGridException("Service not found"));
                int duration = service.getDuration();

                List<Employee> employees = employeeRepository.findByServicesId(serviceId);

                int dayOfWeek = date.getDayOfWeek().getValue() == 7 ? 0 : date.getDayOfWeek().getValue();
                BusinessHours businessHours = businessHoursRepository
                                .findByBusinessIdAndDayOfWeek(service.getBusiness().getId(), dayOfWeek)
                                .orElseThrow(() -> new InvalidAppointmentGridException(
                                                "No se encontraron horarios disponibles"));

                if (!businessHours.getIsWorkingDay()) {
                        throw new InvalidAppointmentGridException("El negocio no opera el día seleccionado");
                }

                List<TimeSlotDto> masterTimeSlots = new ArrayList<>();
                LocalTime morningStart = businessHours.getMorningStart();
                LocalTime morningEnd = businessHours.getMorningEnd();
                if (morningStart != null && morningEnd != null) {
                        LocalTime current = morningStart;
                        while (!current.plusMinutes(duration).isAfter(morningEnd)) {
                                masterTimeSlots.add(TimeSlotDto.builder().startTime(current)
                                                .endTime(current.plusMinutes(duration)).available(true).build());
                                current = current.plusMinutes(duration);
                        }
                }
                LocalTime afternoonStart = businessHours.getAfternoonStart();
                LocalTime afternoonEnd = businessHours.getAfternoonEnd();
                if (afternoonStart != null && afternoonEnd != null) {
                        LocalTime current = afternoonStart;
                        while (!current.plusMinutes(duration).isAfter(afternoonEnd)) {
                                masterTimeSlots.add(TimeSlotDto.builder().startTime(current)
                                                .endTime(current.plusMinutes(duration)).available(true).build());
                                current = current.plusMinutes(duration);
                        }
                }

                List<EmployeeScheduleGridDto> employeeScheduleGridDtos = new ArrayList<>();

                for (Employee employee : employees) {
                        EmployeeWorkSchedule schedule = employeeWorkScheduleRepository
                                        .findByEmployeeIdAndDayOfWeek(employee.getId(), dayOfWeek);
                        List<Appointment> takenAppointments = appointmentRepository
                                        .findByEmployeeIdAndAppointmentDate(employee.getId(), date);

                        List<String> validSlots = appointmentSlotGenerator.generateAvailableSlots(date, duration,
                                        businessHours, schedule, takenAppointments);

                        List<TimeSlotDto> employeeSpecificSlots = new ArrayList<>();
                        for (TimeSlotDto genericSlot : masterTimeSlots) {
                                boolean isAvailableForEmployee = validSlots
                                                .contains(genericSlot.getStartTime().format(TIME_FORMATTER));
                                employeeSpecificSlots.add(TimeSlotDto.builder()
                                                .startTime(genericSlot.getStartTime())
                                                .endTime(genericSlot.getEndTime())
                                                .available(isAvailableForEmployee)
                                                .build());
                        }

                        employeeScheduleGridDtos.add(new EmployeeScheduleGridDto(employee.getId(), employee.getName(),
                                        employeeSpecificSlots));
                }

                return AppointmentGridDto.builder()
                                .serviceName(service.getName())
                                .serviceDuration(duration)
                                .timeSlots(masterTimeSlots)
                                .employeeAvailabilities(employeeScheduleGridDtos)
                                .build();
        }
}
