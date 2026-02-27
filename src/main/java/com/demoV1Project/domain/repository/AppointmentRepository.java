package com.demoV1Project.domain.repository;

import com.demoV1Project.domain.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
        List<Appointment> findByEmployeeIdAndDate(Long id, LocalDateTime date);

        /**
         * Busca citas que se solapan con el rango [start, end) para un empleado dado.
         * Excluye citas canceladas.
         */
        @Query(value = "SELECT a.* FROM appointments a " +
                        "JOIN services s ON a.service_id = s.id " +
                        "WHERE a.employee_id = :employeeId " +
                        "AND a.status <> 'CANCELLED' " +
                        "AND a.date < :endTime " +
                        "AND (a.date + (s.duration * interval '1 minute')) > :startTime", nativeQuery = true)
        List<Appointment> findOverlappingAppointments(
                        @Param("employeeId") Long employeeId,
                        @Param("startTime") LocalDateTime startTime,
                        @Param("endTime") LocalDateTime endTime);
}
