package com.demoV1Project.infrastructure.persistence.impl;

import com.demoV1Project.domain.model.Appointment;
import com.demoV1Project.infrastructure.persistence.AppointmentDao;
import com.demoV1Project.domain.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
@Component
public class AppointmentDaoImpl implements AppointmentDao {

    @Autowired
    public final AppointmentRepository appointmentRepository;

    public AppointmentDaoImpl(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
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
}
