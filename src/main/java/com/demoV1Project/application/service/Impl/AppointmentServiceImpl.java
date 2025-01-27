package com.demoV1Project.application.service.Impl;

import com.demoV1Project.domain.model.Appointment;
import com.demoV1Project.infrastructure.persistence.AppointmentDao;
import com.demoV1Project.application.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private final AppointmentDao appointmentDao;

    public AppointmentServiceImpl(AppointmentDao appointmentDao) {
        this.appointmentDao = appointmentDao;
    }

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
}
