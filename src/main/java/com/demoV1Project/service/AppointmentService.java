package com.demoV1Project.service;


import com.demoV1Project.model.Appointment;

import java.util.List;
import java.util.Optional;

public interface AppointmentService {
    List<Appointment> findAll();
    Optional<Appointment> findById(Long id);
    void save(Appointment appointment);
    // void update(Appointment appointment);
    void deleteById(Long id);

}
