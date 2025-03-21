package com.demoV1Project.application.service.Impl;

import com.demoV1Project.application.service.*;
import com.demoV1Project.domain.dto.AppointmentDto.AppointmentCreateDto;
import com.demoV1Project.domain.dto.AppointmentDto.AppointmentDto;
import com.demoV1Project.domain.model.Appointment;
import com.demoV1Project.domain.model.Business;
import com.demoV1Project.domain.model.Employee;
import com.demoV1Project.domain.model.User;
import com.demoV1Project.infrastructure.persistence.AppointmentDao;
import com.demoV1Project.util.enums.AppointmentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private final AppointmentDao appointmentDao;

    @Autowired
    private final EmployeeService employeeService;

    @Autowired
    private final ServiceService serviceService;

    @Autowired
    private final UserService userService;

    @Autowired
    private final BusinessService businessService;

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
}
