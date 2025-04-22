package com.demoV1Project.infrastructure.persistence;

import com.demoV1Project.domain.model.EmployeeWorkSchedule;

import java.util.List;

public interface EmployeeWorkScheduleDao {
    List<EmployeeWorkSchedule> saveAll(List<EmployeeWorkSchedule> schedules);
    List<EmployeeWorkSchedule> findByEmployeeId(Long employeeId);
    void deleteByEmployeeId(Long employeeId);
}
