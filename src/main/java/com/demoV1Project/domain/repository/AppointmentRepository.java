package com.demoV1Project.domain.repository;

import com.demoV1Project.domain.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
}
