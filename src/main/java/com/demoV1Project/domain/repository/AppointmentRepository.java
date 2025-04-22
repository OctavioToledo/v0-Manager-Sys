package com.demoV1Project.domain.repository;

import com.demoV1Project.domain.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

   // @Query("SELECT a FROM Appointment a JOIN FETCH a.business WHERE a.id = :id")
    // Optional<Appointment> findByIdWithBusiness(@Param("id") Long id);

}
