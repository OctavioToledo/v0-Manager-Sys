package com.demoV1Project.controllers;

import com.demoV1Project.dto.AppointmentDto;
import com.demoV1Project.enums.AppointmentStatus;
import com.demoV1Project.model.Appointment;
import com.demoV1Project.model.Employee;
import com.demoV1Project.model.Service;
import com.demoV1Project.model.User;
import com.demoV1Project.service.AppointmentService;
import com.demoV1Project.service.EmployeeService;
import com.demoV1Project.service.ServiceService;
import com.demoV1Project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v0/appointment")
@RequiredArgsConstructor
public class AppointmentController {

    @Autowired
    private final AppointmentService appointmentService;

    @Autowired
    private final UserService userService;

    @Autowired
    private final EmployeeService employeeService;

    @Autowired
    private final ServiceService serviceService;

    @GetMapping("/findAll")
    public ResponseEntity<?> findAll() {
        List<AppointmentDto> appointmentList = appointmentService.findAll()
                .stream()
                .map(appointment -> AppointmentDto.builder()
                        .id(appointment.getId())
                        .status(appointment.getStatus().toString()) // Convertir enum a String
                        .date(appointment.getDate())
                        .employeeId(appointment.getEmployee().getId())
                        .serviceId(appointment.getService().getId())
                        .userId(appointment.getUser().getId())
                        .build())
                .toList();

        return ResponseEntity.ok(appointmentList);
    }


    @GetMapping("/find/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<Appointment> appointmentOptional = appointmentService.findById(id);

        if (appointmentOptional.isPresent()) {
            Appointment appointment = appointmentOptional.get();
            AppointmentDto appointmentDto = AppointmentDto.builder()
                    .id(appointment.getId())
                    .status(appointment.getStatus().toString()) // Convertir enum a String
                    .date(appointment.getDate())
                    .employeeId(appointment.getEmployee().getId())
                    .serviceId(appointment.getService().getId())
                    .userId(appointment.getUser().getId())
                    .build();

            return ResponseEntity.ok(appointmentDto);
        }
        return ResponseEntity.notFound().build();
    }


    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody AppointmentDto appointmentDto) throws URISyntaxException {
        // Validar y obtener Employee
        if (appointmentDto.getEmployeeId() == null) {
            return ResponseEntity.badRequest().body("Employee ID is required");
        }
        Optional<Employee> employeeOptional = employeeService.findById(appointmentDto.getEmployeeId());
        if (employeeOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Employee not found");
        }
        Employee employee = employeeOptional.get();

        // Validar y obtener Service
        if (appointmentDto.getServiceId() == null) {
            return ResponseEntity.badRequest().body("Service ID is required");
        }
        Optional<Service> serviceOptional = serviceService.findById(appointmentDto.getServiceId());
        if (serviceOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Service not found");
        }
        Service service = serviceOptional.get();

        // Validar y obtener User
        if (appointmentDto.getUserId() == null) {
            return ResponseEntity.badRequest().body("User ID is required");
        }
        Optional<User> userOptional = userService.findById(appointmentDto.getUserId());
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }
        User user = userOptional.get();

        // Convertir el String del status al enum
        AppointmentStatus status;
        try {
            status = AppointmentStatus.valueOf(appointmentDto.getStatus());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid status value");
        }

        // Crear y guardar el Appointment
        Appointment appointment = Appointment.builder()
                .status(status)
                .date(appointmentDto.getDate())
                .employee(employee)
                .service(service)
                .user(user)
                .build();

        appointmentService.save(appointment);
        return ResponseEntity.created(new URI("/api/v0/appointment/save")).build();
    }


    @DeleteMapping("/delete/{id}")
        public ResponseEntity<?> deleteById(@PathVariable Long id) {
            if (id != null) {
                appointmentService.deleteById(id);
                return ResponseEntity.ok("Appointment Deleted");
            }
            return ResponseEntity.badRequest().build();
        }
    }

