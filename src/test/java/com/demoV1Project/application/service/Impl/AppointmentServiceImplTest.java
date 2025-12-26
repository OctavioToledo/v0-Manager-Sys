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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
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

    @Mock private AppointmentDao appointmentDao;
    @Mock private AppointmentRepository appointmentRepository;
    @Mock private EmployeeService employeeService;
    @Mock private EmployeeRepository employeeRepository;
    @Mock private ServiceRepository serviceRepository;
    @Mock private ServiceService serviceService;
    @Mock private UserService userService;
    @Mock private BusinessService businessService;
    @Mock private BusinessHoursRepository businessHoursRepository;
    @Mock private EmployeeWorkScheduleRepository employeeWorkScheduleRepository;

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
                .date(LocalDateTime.now())
                .status("PENDING")
                .build();

        Employee employee = new Employee();
        employee.setId(1L);
        Business business = new Business();
        business.setId(1L);
        com.demoV1Project.domain.model.Service service = new com.demoV1Project.domain.model.Service();
        service.setId(1L);
        User user = new User();
        user.setId(1L);

        when(employeeService.findById(1L)).thenReturn(Optional.of(employee));
        when(businessService.findById(1L)).thenReturn(Optional.of(business));
        when(serviceService.findById(1L)).thenReturn(Optional.of(service));
        when(userService.findById(1L)).thenReturn(Optional.of(user));

        // Act
        Appointment result = appointmentService.createAndSaveAppointment(dto);

        // Assert
        assertNotNull(result);
        assertEquals(AppointmentStatus.PENDING, result.getStatus());
        verify(appointmentDao, times(1)).save(any(Appointment.class));
    }

    @Test
    void createAndSaveAppointment_EmployeeNotFound_ThrowsException() {
        // Arrange
        AppointmentCreateDto dto = AppointmentCreateDto.builder().employeeId(1L).build();
        when(employeeService.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            appointmentService.createAndSaveAppointment(dto);
        });
        assertEquals("Employee not found", exception.getMessage());
    }

    @Test
    void getAppointmentGrid_Success() {
        // Arrange
        Long serviceId = 1L;
        LocalDateTime date = LocalDateTime.of(2023, 10, 25, 10, 0); // Wednesday

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
        businessHours.setOpeningMorningTime(LocalTime.of(9, 0));
        businessHours.setClosingMorningTime(LocalTime.of(12, 0)); // 9-10, 10-11, 11-12
        when(businessHoursRepository.findByBusinessIdAndDayOfWeek(eq(10L), any(DayOfWeek.class)))
                .thenReturn(Optional.of(businessHours));

        EmployeeWorkSchedule schedule = new EmployeeWorkSchedule();
        schedule.setOpeningMorningTime(LocalTime.of(9, 0));
        schedule.setClosingMorningTime(LocalTime.of(12, 0));
        when(employeeWorkScheduleRepository.findByEmployeeIdAndDayOfWeek(eq(100L), any(DayOfWeek.class)))
                .thenReturn(schedule);

        when(appointmentRepository.findByEmployeeIdAndDate(eq(100L), any(LocalDateTime.class)))
                .thenReturn(new ArrayList<>());

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
