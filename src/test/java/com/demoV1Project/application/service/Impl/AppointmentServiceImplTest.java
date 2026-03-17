package com.demoV1Project.application.service.Impl;

import com.demoV1Project.application.service.*;
import com.demoV1Project.domain.dto.AppointmentDto.AppointmentCreateDto;
import com.demoV1Project.domain.dto.AppointmentGridDto.AppointmentGridDto;
import com.demoV1Project.domain.dto.AppointmentGridDto.EmployeeScheduleGridDto;
import com.demoV1Project.domain.model.*;
import com.demoV1Project.domain.repository.*;
import com.demoV1Project.util.enums.AppointmentStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceImplTest {

    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private EmployeeService employeeService;
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private ServiceRepository serviceRepository;
    @Mock
    private ServiceService serviceService;
    @Mock
    private UserService userService;
    @Mock
    private BusinessService businessService;
    @Mock
    private BusinessHoursRepository businessHoursRepository;
    @Mock
    private EmployeeWorkScheduleRepository employeeWorkScheduleRepository;
    @Mock
    private AppointmentSlotGenerator appointmentSlotGenerator;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    @Test
    void createAndSaveAppointment_Success() {
        // Arrange
        AppointmentCreateDto dto = AppointmentCreateDto.builder()
                .employeeId(1L)
                .businessId(1L)
                .serviceId(1L)
                .userId(1L)
                .appointmentDate(LocalDate.now())
                .startTime("09:00")
                .status("PENDING")
                .build();

        Employee employee = new Employee();
        employee.setId(1L);
        Business business = new Business();
        business.setId(1L);
        employee.setBusiness(business);
        com.demoV1Project.domain.model.Service service = new com.demoV1Project.domain.model.Service();
        service.setId(1L);
        service.setBusiness(business);
        service.setDuration(30); // Prevent NPE on start.plusMinutes(service.getDuration())
        User user = new User();
        user.setId(1L);

        when(employeeService.findById(1L)).thenReturn(Optional.of(employee));
        when(businessService.findById(1L)).thenReturn(Optional.of(business));
        when(serviceService.findById(1L)).thenReturn(Optional.of(service));
        when(userService.findById(1L)).thenReturn(Optional.of(user));
        when(appointmentRepository.save(any(Appointment.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Appointment result = appointmentService.createPendingAppointment(dto);

        // Assert
        assertNotNull(result);
        assertEquals(AppointmentStatus.PENDING, result.getStatus());
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }

    @Test
    void createAndSaveAppointment_EmployeeNotFound_ThrowsException() {
        // Arrange
        AppointmentCreateDto dto = AppointmentCreateDto.builder().employeeId(1L).build();
        when(employeeService.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            appointmentService.createPendingAppointment(dto);
        });
        assertEquals("Employee not found", exception.getMessage());
    }

    @Test
    void getAppointmentGrid_Success() {
        // Arrange
        Long serviceId = 1L;
        LocalDate date = LocalDate.of(2023, 10, 25); // Wednesday

        Business business = new Business();
        business.setId(10L);

        com.demoV1Project.domain.model.Service service = new com.demoV1Project.domain.model.Service();
        service.setId(serviceId);
        service.setDuration(60);
        service.setName("Haircut");
        service.setBusiness(business);

        when(serviceService.findById(serviceId)).thenReturn(Optional.of(service));

        Employee employee = new Employee();
        employee.setId(100L);
        employee.setName("John Doe");
        List<Employee> employees = Collections.singletonList(employee);
        when(employeeRepository.findByServicesId(serviceId)).thenReturn(employees);

        BusinessHours businessHours = new BusinessHours();
        businessHours.setIsWorkingDay(true);
        businessHours.setMorningStart(LocalTime.of(9, 0));
        businessHours.setMorningEnd(LocalTime.of(12, 0)); // 9-10, 10-11, 11-12
        when(businessHoursRepository.findByBusinessIdAndDayOfWeek(eq(10L), anyInt()))
                .thenReturn(Optional.of(businessHours));

        EmployeeWorkSchedule schedule = new EmployeeWorkSchedule();
        schedule.setIsWorkingDay(true);
        schedule.setMorningStart(LocalTime.of(9, 0));
        schedule.setMorningEnd(LocalTime.of(12, 0));
        when(employeeWorkScheduleRepository.findByEmployeeIdAndDayOfWeek(eq(100L), anyInt()))
                .thenReturn(schedule);

        when(appointmentRepository.findByEmployeeIdAndAppointmentDate(eq(100L), any(LocalDate.class)))
                .thenReturn(new ArrayList<>());

        when(appointmentSlotGenerator.generateAvailableSlots(any(), anyInt(), any(), any(), any()))
                .thenReturn(List.of("10:00", "11:00"));

        // Act
        AppointmentGridDto grid = appointmentService.getAppointmentGrid(serviceId, date);

        // Assert
        assertNotNull(grid);
        assertEquals("Haircut", grid.getServiceName());
        assertEquals(60, grid.getServiceDuration());
        assertFalse(grid.getTimeSlots().isEmpty());
        assertEquals(1, grid.getEmployeeAvailabilities().size());

        EmployeeScheduleGridDto empGrid = grid.getEmployeeAvailabilities().get(0);
        assertEquals("John Doe", empGrid.getEmployeeName());
        // 9:00, 10:00, 11:00 should be generated
    }
}
