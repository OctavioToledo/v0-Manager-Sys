package com.demoV1Project.infrastructure.persistence;

import com.demoV1Project.domain.model.Appointment;

import java.util.List;
import java.util.Optional;

public interface AppointmentDao {
    List<Appointment> findAll();
    Optional<Appointment> findById(Long id);
    void save(Appointment appointment);
    void deleteById(Long id);
    //void update(Appointment appointment);

}
