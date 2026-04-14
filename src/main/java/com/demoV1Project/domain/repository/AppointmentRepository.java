package com.demoV1Project.domain.repository;

import com.demoV1Project.domain.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
        List<Appointment> findByEmployeeIdAndAppointmentDate(Long employeeId, LocalDate appointmentDate);

        List<Appointment> findByBusinessIdAndAppointmentDate(Long businessId, LocalDate appointmentDate);

        List<Appointment> findByBusinessIdAndAppointmentDateBetween(Long businessId, LocalDate startDate, LocalDate endDate);

        @Query(value = "SELECT a.* FROM appointments a " +
                        "WHERE a.employee_id = :employeeId " +
                        "AND a.status <> 'CANCELLED' " +
                        "AND a.status <> 'REJECTED' " +
                        "AND a.appointment_date = :date " +
                        "AND CAST(a.start_time AS time) < CAST(:endTime AS time) " +
                        "AND CAST(a.end_time AS time) > CAST(:startTime AS time)", nativeQuery = true)
        List<Appointment> findOverlappingAppointments(
                        @Param("employeeId") Long employeeId,
                        @Param("date") LocalDate date,
                        @Param("startTime") String startTime,
                        @Param("endTime") String endTime);

        @Query(value = "SELECT COUNT(a.id) FROM appointments a " +
                        "WHERE a.employee_id = :employeeId " +
                        "AND a.status = 'APPROVED' " +
                        "AND a.appointment_date = :date " +
                        "AND CAST(a.start_time AS time) < CAST(:endTime AS time) " +
                        "AND CAST(a.end_time AS time) > CAST(:startTime AS time)", nativeQuery = true)
        int countOverlappingApprovedAppointments(
                        @Param("employeeId") Long employeeId,
                        @Param("date") LocalDate date,
                        @Param("startTime") String startTime,
                        @Param("endTime") String endTime);
}
