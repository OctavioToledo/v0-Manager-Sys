package com.demoV1Project.domain.repository;

import com.demoV1Project.domain.model.EmployeeWorkSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;
@Repository
public interface EmployeeWorkScheduleRepository extends JpaRepository<EmployeeWorkSchedule, Long> {
    List<EmployeeWorkSchedule> findByEmployeeId(Long employeeId);
    void deleteByEmployeeId(Long employeeId);

    EmployeeWorkSchedule findByEmployeeIdAndDayOfWeek(Long id, DayOfWeek dayOfWeek);
}
